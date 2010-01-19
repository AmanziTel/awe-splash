/* AWE - Amanzi Wireless Explorer
 * http://awe.amanzi.org
 * (C) 2008-2009, AmanziTel AB
 *
 * This library is provided under the terms of the Eclipse Public License
 * as described at http://www.eclipse.org/legal/epl-v10.html. Any use,
 * reproduction or distribution of the library constitutes recipient's
 * acceptance of this agreement.
 *
 * This library is distributed WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.amanzi.neo.loader;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.amanzi.neo.core.INeoConstants;
import org.amanzi.neo.core.NeoCorePlugin;
import org.amanzi.neo.core.enums.GeoNeoRelationshipTypes;
import org.amanzi.neo.core.enums.GisTypes;
import org.amanzi.neo.core.utils.NeoUtils;
import org.amanzi.neo.index.MultiPropertyIndex;
import org.amanzi.neo.index.MultiPropertyIndex.MultiTimeIndexConverter;
import org.amanzi.neo.loader.etsi.commands.AbstractETSICommand;
import org.amanzi.neo.loader.etsi.commands.CommandSyntax;
import org.amanzi.neo.loader.etsi.commands.ETSICommandPackage;
import org.amanzi.neo.loader.internal.NeoLoaderPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.swt.widgets.Display;
import org.neo4j.api.core.Direction;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.ReturnableEvaluator;
import org.neo4j.api.core.StopEvaluator;
import org.neo4j.api.core.Transaction;
import org.neo4j.api.core.TraversalPosition;
import org.neo4j.api.core.Traverser.Order;

/**
 * Loader of ETSI data
 * 
 * @author Lagutko_N
 * @since 1.0.0
 */
public class ETSILoader extends DriveLoader {
	
	public class Call {
		
		private long callSetupBeginTime;
		
		private long callSetupEndTime;
		
		private long callEndTime;

		/**
		 * @return Returns the callBeginTime.
		 */
		public long getCallSetupBeginTime() {
			return callSetupBeginTime;
		}

		/**
		 * @param callSetupBeginTime The callBeginTime to set.
		 */
		public void setCallSetupBeginTime(long callSetupBeginTime) {
			this.callSetupBeginTime = callSetupBeginTime;
		}

		/**
		 * @return Returns the callEndTime.
		 */
		public long getCallSetupEndTime() {
			return callSetupEndTime;
		}

		/**
		 * @param callSetupEndTime The callEndTime to set.
		 */
		public void setCallSetupEndTime(long callSetupEndTime) {
			this.callSetupEndTime = callSetupEndTime;
		}

		/**
		 * @return Returns the callEndTime.
		 */
		public long getCallEndTime() {
			return callEndTime;
		}

		/**
		 * @param callEndTime The callEndTime to set.
		 */
		public void setCallEndTime(long callEndTime) {
			this.callEndTime = callEndTime;
		}
		
	}
	
	private enum CallEvents {
		CALL_SETUP_BEGIN("atd"),
		CALL_SETUP_END("CTOCP"),
		CALL_END("ATH");
		
		private String commandName;
		
		private CallEvents(String commandName) {
			this.commandName = commandName;
		}
		
		public static CallEvents getCallEvent(String commandName) {
			for (CallEvents event : values()) {
				if (event.commandName.equals(commandName)) {
					return event;
				}
			}
			
			return null;
		}
	}
	
	/*
	 * 
	 */
	private static final String UNSOLICITED = "<UNSOLICITED>";
	
	/*
	 * ETSI log file extension
	 */
	private static final String ETSI_LOG_FILE_EXTENSION = ".log";

	/*
	 * Timestamp format for ETSI log files
	 */
	private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss,SSS";
	
	/*
	 * Formatter for timestamp
	 */
	private SimpleDateFormat timestampFormat;
	
	/*
	 * Previous Mp Node
	 */
	private Node previousMpNode;
	
	/*
	 * Previous Ms Node 
	 */
	private Node previousMsNode;
	
	/*
	 * List of mm nodes
	 */
	private ArrayList<Node> mmNodes = new ArrayList<Node>();
	
	/*
	 * Current name of file node
	 */
	private String currentFileName;
	
	/*
	 * Is new file proccesses
	 */
	private boolean newFile = true;
	
	/*
	 * Network node for Probes data
	 */
	private Node networkNode;
	
	/*
	 * Name of directory
	 */
	private String directoryName;
	
	/*
	 * Node for current directory
	 */
	private Node currentDirectoryNode;
	
	private Node previousCallNode;
	
	private Call call;
	
	/**
	 * Creates a loader
	 * 
	 * @param directoryName name of directory to import
	 * @param display
	 * @param datasetName name of dataset
	 */
	public ETSILoader(String directoryName, Display display, String datasetName, String networkName) {
		if (datasetName == null) {
			int startIndex = directoryName.lastIndexOf(File.separator);
			if (startIndex < 0) {
				startIndex = 0;
			}
			else {
				startIndex++;
			}
			datasetName = directoryName.substring(startIndex);
		}
		
		this.directoryName = directoryName;
		this.filename = directoryName;		
		
		initialize("ETSI", null, directoryName, display, datasetName);
		initializeNetwork(networkName);
		
		addDriveIndexes();
		
		timestampFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
	}
	
	/**
	 * Initializes Network for probes
	 *
	 * @param networkName name of network
	 */
	private void initializeNetwork(String networkName) {
		String oldBasename = basename;
		if ((networkName == null) || (networkName.length() == 0)) {
			networkName = basename + " Probes";
		}
		else {
			networkName = networkName.trim();
		}
		
		basename = networkName;
		this.networkNode = findOrCreateNetworkNode(null, true);			
		findOrCreateGISNode(this.networkNode, GisTypes.NETWORK.getHeader());
		gis = null;		
		basename = oldBasename;
	}
	
	@Override
	public void run(IProgressMonitor monitor) throws IOException {
		monitor.beginTask("Loading ETSI data", 2);
		monitor.subTask("Searching for files to load");
		ArrayList<File> allFiles = getAllLogFilePathes(filename);
		
		monitor = SubMonitor.convert(monitor, allFiles.size());
		monitor.beginTask("Loading ETSI data", allFiles.size());
		for (File logFile : allFiles) {
			monitor.subTask("Loading file " + logFile.getAbsolutePath());
			
			filename = logFile.getAbsolutePath();
			currentDirectoryNode = findOrCreateDirectoryNode(null, logFile.getParentFile());
			
			newFile = true;
			typedProperties = null;
	
			super.run(null);
			
			monitor.worked(1);
		}
		
		cleanupGisNode();
		finishUpGis(getDatasetNode());
		
		basename = dataset;
		printStats(false);
	}
	
	/**
	 * Searches for Directory node and creates it if it cannot be found
	 *
	 * @param parentDirectoryNode node of parent directory
	 * @param directoryFile file of Directory
	 * @return founded directory node
	 */
	private Node findOrCreateDirectoryNode(Node parentDirectoryNode, File directoryFile) {
		Transaction tx = neo.beginTx();
		try {			
			final String directoryName = directoryFile.getName();
			String directoryPath = directoryFile.getPath();
			
			if (!directoryPath.equals(this.directoryName)) {
				parentDirectoryNode = findOrCreateDirectoryNode(parentDirectoryNode, directoryFile.getParentFile());
			}
		
			if (parentDirectoryNode == null) {
				parentDirectoryNode = getDatasetNode();
				if (parentDirectoryNode == null) {
					parentDirectoryNode = findOrCreateDatasetNode(neo.getReferenceNode(), dataset);
					datasetNode = parentDirectoryNode;
					findOrCreateGISNode(parentDirectoryNode, GisTypes.DRIVE.getHeader());
				}
			}
		
			Iterator<Node> directoryIterator = parentDirectoryNode.traverse(Order.BREADTH_FIRST, StopEvaluator.END_OF_GRAPH, new ReturnableEvaluator() {
			
				@Override
				public boolean isReturnableNode(TraversalPosition currentPos) {
					return currentPos.currentNode().hasProperty(INeoConstants.PROPERTY_TYPE_NAME) &&
						   currentPos.currentNode().getProperty(INeoConstants.PROPERTY_TYPE_NAME).equals(INeoConstants.DIRECTORY_TYPE_NAME) &&
						   currentPos.currentNode().hasProperty(INeoConstants.PROPERTY_NAME_NAME) &&
						   currentPos.currentNode().getProperty(INeoConstants.PROPERTY_NAME_NAME).equals(directoryName);
				}
			}, GeoNeoRelationshipTypes.NEXT, Direction.OUTGOING).iterator();
		
			if (directoryIterator.hasNext()) {
				tx.success();
				return directoryIterator.next();
			}
			else {
				Node directoryNode = neo.createNode();
				directoryNode.setProperty(INeoConstants.PROPERTY_TYPE_NAME, INeoConstants.DIRECTORY_TYPE_NAME);
				directoryNode.setProperty(INeoConstants.PROPERTY_NAME_NAME, directoryName);
				directoryNode.setProperty(INeoConstants.PROPERTY_FILENAME_NAME, directoryPath);
				
				parentDirectoryNode.createRelationshipTo(directoryNode, GeoNeoRelationshipTypes.NEXT);
				
				tx.success();
				return directoryNode;
			}
		}
		catch (Exception e) {
			NeoLoaderPlugin.exception(e);
			tx.failure();
			return null;
		}
		finally {
			tx.finish();
		}		
	}
	
	@Override
	protected final void findOrCreateFileNode(Node mp) {
		int startIndex = filename.lastIndexOf(File.separator);
		if (startIndex < 0) {
			startIndex = 0;
		}
		else {
			startIndex++;
		}
		
		//we should load all files #1, #2 and #3 in a single file node
		if (newFile) {
			String tempName = filename.substring(startIndex, filename.lastIndexOf("#"));
			if ((currentFileName == null) || (!currentFileName.equals(tempName))) {
				currentFileName = tempName;
				filename = tempName;
				basename = filename;
				file = null;
				previousMpNode = null;
			}
			newFile = false;
		}
		
		if (file == null) {
			Iterator<Node> files = currentDirectoryNode.traverse(Order.BREADTH_FIRST, StopEvaluator.DEPTH_ONE, new ReturnableEvaluator() {
				
				@Override
				public boolean isReturnableNode(TraversalPosition currentPos) {
					return currentPos.currentNode().hasProperty(INeoConstants.PROPERTY_TYPE_NAME) &&
						   currentPos.currentNode().getProperty(INeoConstants.PROPERTY_TYPE_NAME).equals(INeoConstants.FILE_TYPE_NAME) &&
						   currentPos.currentNode().hasProperty(INeoConstants.PROPERTY_NAME_NAME) &&
						   currentPos.currentNode().getProperty(INeoConstants.PROPERTY_NAME_NAME).equals(basename);
				}
			}, GeoNeoRelationshipTypes.NEXT, Direction.OUTGOING).iterator();
			
			if (files.hasNext()) {
				file = files.next();
			}
			else {
				file = neo.createNode();
				file.setProperty(INeoConstants.PROPERTY_TYPE_NAME, INeoConstants.FILE_TYPE_NAME);
				file.setProperty(INeoConstants.PROPERTY_NAME_NAME, basename);
				file.setProperty(INeoConstants.PROPERTY_FILENAME_NAME, filename);
				currentDirectoryNode.createRelationshipTo(file, GeoNeoRelationshipTypes.NEXT);
			}
			file.createRelationshipTo(mp, GeoNeoRelationshipTypes.NEXT);
		}		
	}
	
	@Override
	protected void parseLine(String line) {
		StringTokenizer tokenizer = new StringTokenizer(line, "|");
		if (!tokenizer.hasMoreTokens()) {
			return;
		}
		
		//get a timestamp
		String timestamp = tokenizer.nextToken();
		
		//try to parse timestamp
		long timestampValue;
		try {
			timestampValue = timestampFormat.parse(timestamp).getTime();
		}
		catch (ParseException e) {
			error(e.getMessage());
			return;
		}
		
		if (!tokenizer.hasMoreTokens()) {
			//if no more tokens than skip parsing
			return;
		}
		
		//next is a kind of command, we should skip it
		tokenizer.nextToken();
		
		if (!tokenizer.hasMoreTokens()) {
			//if no more tokens than skip parsing
			return;
		}

		//get a name of command
		String commandName = tokenizer.nextToken();
		
		if (!tokenizer.hasMoreTokens()) {
			//if last token is command name than it's Port.write command, do not proccess it
			return;
		}
		
		if (ETSICommandPackage.isETSICommand(commandName) || (commandName.equals(UNSOLICITED))) {
			CommandSyntax syntax = ETSICommandPackage.getCommandSyntax(commandName);
			if (syntax == CommandSyntax.SET) { 
				int equalsIndex = commandName.indexOf("=");
				tokenizer = new StringTokenizer(commandName.substring(equalsIndex).trim());
				commandName = commandName.substring(0, equalsIndex);
			}
			
			AbstractETSICommand command = ETSICommandPackage.getCommand(commandName, syntax);
			if (command == null) {
				return;
			}
			
			//get a real name of command without set or get postfix
			Node mpNode = createMpNode(timestampValue);
			if (mpNode != null) {
				//parse parameters of command
				HashMap<String, Object> parameters = null;
				
				parameters = processCommand(timestampValue, command, syntax, tokenizer);
				if (command != null) {
					commandName = command.getName();
				}
				else {
					commandName = ETSICommandPackage.getRealCommandName(commandName);
				}
					
				if (!commandName.equals(UNSOLICITED)) {
					createMsNode(mpNode, commandName, parameters);
				}
				
				if (syntax == CommandSyntax.EXECUTE) {
					while (tokenizer.hasMoreTokens()) {
						String maybeTimestamp = tokenizer.nextToken();						
						if (maybeTimestamp.startsWith("~")) {
							timestamp = maybeTimestamp;
						}
						else if (maybeTimestamp.startsWith("+")) {
							int colonIndex = maybeTimestamp.indexOf(":");
							commandName = maybeTimestamp.substring(1, colonIndex);
							StringTokenizer paramTokenizer = new StringTokenizer(maybeTimestamp.substring(colonIndex + 1).trim());
							syntax = ETSICommandPackage.getCommandSyntax(commandName);
							command = ETSICommandPackage.getCommand(commandName, syntax);
						
							if (command != null) {
								//should be a result of command
								parameters = command.getResults(syntax, paramTokenizer);
								if (command != null) {
									commandName = command.getName();
								}
								else {
									commandName = ETSICommandPackage.getRealCommandName(commandName);
								}
							}
							createMsNode(mpNode, commandName, parameters);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Creates Ms Node
	 *
	 * @param mpNode parent mp node
	 * @param commandName name of command
	 * @param parameters parameters of command
	 */
	private void createMsNode(Node mpNode, String commandName, HashMap<String, Object> parameters) {
		Node msNode = neo.createNode();
		msNode.setProperty(INeoConstants.COMMAND_PROPERTY_NAME, commandName);
		msNode.setProperty(INeoConstants.PROPERTY_TYPE_NAME, INeoConstants.HEADER_MS);
		
		if (parameters != null) {
			for (String name : parameters.keySet()) {
				Object value = parameters.get(name);
				if (!(value instanceof List<?>)) {
					//add value to statistics
					Header header = headers.get(name);
					if (header == null) {
						header = new Header(name, name, 1);
						
						headers.put(name, header);
					}
					header.parseCount++;
					header.incValue(value);
					header.incType(value.getClass());
					
					msNode.setProperty(name, parameters.get(name));
				}
				else {
					setPropertyToMmNodes(msNode, name, (List<?>)value);
				}
			}
		}
		
		mmNodes.clear();
		
		mpNode.createRelationshipTo(msNode, GeoNeoRelationshipTypes.CHILD);
		
		if (previousMsNode != null) {
			previousMsNode.createRelationshipTo(msNode, GeoNeoRelationshipTypes.NEXT);
		}
		previousMsNode = msNode;
	}
	
	/**
	 * Creates Mm nodes for multiple properties of Ms node
	 *
	 * @param msNode parent ms node
	 * @param name name of property
	 * @param properties list of values for this property
	 */
	private void setPropertyToMmNodes(Node msNode, String name, List<?> properties) {
		Node previousMmNode = null;
		
		for (int i = 0; i < properties.size(); i++) {
			Node mmNode = null; 
			if (mmNodes.size() > i) {
				mmNode = mmNodes.get(i);
			}
			
			if (mmNode == null) {
				mmNode = neo.createNode();
				mmNode.setProperty(INeoConstants.PROPERTY_TYPE_NAME, "mm");	
				
				msNode.createRelationshipTo(mmNode, GeoNeoRelationshipTypes.CHILD);
			}
			
			if (previousMmNode != null) {
				previousMmNode.createRelationshipTo(mmNode, GeoNeoRelationshipTypes.NEXT);
			}
			previousMmNode = mmNode;				
			
			mmNodes.add(mmNode);
			Object value = properties.get(i);
			if (value != null) {
				mmNode.setProperty(name, properties.get(i)); 
			}						
		}
	}
	
	/**
	 * Creates Mp node
	 *
	 * @param timestamp timestamp
	 * @return created node
	 */
	private Node createMpNode(long timestamp) {
		Node mpNode = neo.createNode();
		mpNode.setProperty(INeoConstants.PROPERTY_TYPE_NAME, INeoConstants.HEADER_M);
		
		updateTimestampMinMax(timestamp);
			
		mpNode.setProperty(INeoConstants.PROPERTY_TIMESTAMP_NAME, timestamp);
		
		index(mpNode);
		findOrCreateFileNode(mpNode);
		
		if (previousMpNode != null) {
			previousMpNode.createRelationshipTo(mpNode, GeoNeoRelationshipTypes.NEXT);
		}
		previousMpNode = mpNode;
		previousMsNode = null;
		
		return mpNode;
	}
	
	/**
	 * Calculates list of files to import
	 *
	 * @param directoryName directory to import
	 * @return list of files to import
	 */
	private ArrayList<File> getAllLogFilePathes(String directoryName) {
		File directory = new File(directoryName);
		ArrayList<File> result = new ArrayList<File>();
		
		for (File childFile : directory.listFiles()) {
			if (childFile.isDirectory()) {
				result.addAll(getAllLogFilePathes(childFile.getAbsolutePath()));
			}
			else if (childFile.isFile() &&
					 childFile.getName().endsWith(ETSI_LOG_FILE_EXTENSION)) {
				result.add(childFile);
			}
		}
		
		return result;
		
	}
	
	/**
	 * Add Timestamp index
	 */
	private void addDriveIndexes() {
        try {
            addIndex(INeoConstants.HEADER_M, new MultiPropertyIndex<Long>(NeoUtils.getTimeIndexName(dataset),
                    new String[] {INeoConstants.PROPERTY_TIMESTAMP_NAME},
                    new MultiTimeIndexConverter(), 10));            
        } catch (IOException e) {
            throw (RuntimeException)new RuntimeException().initCause(e);
        }
    }
	
	@Override
	protected boolean haveHeaders() {
        return true;
	}
	
	private HashMap<String, Object> processCommand(long timestamp, AbstractETSICommand command, CommandSyntax syntax, StringTokenizer tokenizer) {
		HashMap<String, Object> result = command.getResults(syntax, tokenizer);
		
		if (command.isCallCommand()) {
			String commandName = command.getName();
			
			CallEvents event = CallEvents.getCallEvent(commandName);
			
			processCallEvent(event, timestamp);
		}
		
		return result;
	}
	
	private void processCallEvent(CallEvents event, long timestamp) {
		switch (event) {
		case CALL_SETUP_BEGIN:
			call = new Call();
			call.setCallSetupBeginTime(timestamp);
			break;
		case CALL_SETUP_END:
			call.setCallSetupEndTime(timestamp);
			break;
		case CALL_END:
			call.setCallEndTime(timestamp);
			saveCall();
			break;
		}
	}
	
	private void saveCall() {
		if (call != null) {
			Node callNode = createCallNode(previousCallNode);
			
			long setupDuration = call.getCallSetupEndTime() - call.getCallSetupBeginTime();
			long callDuration = call.getCallEndTime() - call.getCallSetupBeginTime();
			
			callNode.setProperty("setupDuration", setupDuration);
			callNode.setProperty("callDuration", callDuration);
			
			previousCallNode = callNode;
		}
	}

	private Node createCallNode(Node previousNode) {
		Transaction transaction = neo.beginTx();
		Node result = null;
		try {
			result = neo.createNode();
			result.setProperty(INeoConstants.PROPERTY_TYPE_NAME, INeoConstants.CALL_TYPE_NAME);
			
			if (previousNode != null) {
				previousNode.createRelationshipTo(result, GeoNeoRelationshipTypes.NEXT);
			}
			
			transaction.success();
		}
		catch (Exception e) {
			NeoCorePlugin.error(null, e);
		}
		finally {
			transaction.finish();
		}
		
		return previousNode;
	}
}
