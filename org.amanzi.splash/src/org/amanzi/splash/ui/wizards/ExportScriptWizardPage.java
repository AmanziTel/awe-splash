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
package org.amanzi.splash.ui.wizards;

import org.amanzi.splash.swing.Cell;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.rubypeople.rdt.core.ISourceFolder;
import org.rubypeople.rdt.core.RubyModelException;
import org.rubypeople.rdt.internal.ui.wizards.NewWizardMessages;
import org.rubypeople.rdt.ui.wizards.NewFileWizardPage;

/**
 * WizardPage for Exporting Scripts
 * 
 * @author Lagutko_N
 *
 */

public class ExportScriptWizardPage extends NewFileWizardPage {
	
	//cell for export
	private Cell cell;
	
	public ExportScriptWizardPage(Cell cell) {
		super();
		this.cell = cell;
		fScriptNameDialogField.setText(getDefaultScriptName());
	}

	public void setSourceFolder(ISourceFolder root, boolean canBeModified) {
		super.setSourceFolder(root, canBeModified);
		fContainerDialogField.setEnabled(false);
	}
	
	protected String getDefaultScriptName() {
		//default script name contains ID of cell
		if (cell != null) {
			return "script" + cell.getCellID() + ".rb";
		}
		else {
			return "";
		}
	}
	
	public void createScript(IProgressMonitor monitor) throws RubyModelException {
		if (monitor == null) {
			monitor= new NullProgressMonitor();
		}

		monitor.beginTask(NewWizardMessages.NewTypeWizardPage_operationdesc, 8); 
		
		ISourceFolder pack= getSourceFolder();

		monitor.worked(1);
		
		try {				
			String cuName= getRubyScriptName();
			//set definition of cell as content of script
			String contents = cell.getDefinition().toString();
			fCreatedScript = pack.createRubyScript(cuName, contents, false, new SubProgressMonitor(monitor, 2)); //$NON-NLS-1$
		} finally {

			monitor.done();
		}
		
	}
	
}
