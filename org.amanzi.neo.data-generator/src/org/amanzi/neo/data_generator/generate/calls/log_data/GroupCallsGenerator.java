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

package org.amanzi.neo.data_generator.generate.calls.log_data;

import java.util.ArrayList;
import java.util.List;

import org.amanzi.neo.data_generator.data.calls.Call;
import org.amanzi.neo.data_generator.data.calls.CallData;
import org.amanzi.neo.data_generator.data.calls.CallGroup;
import org.amanzi.neo.data_generator.data.calls.CallParameterNames;
import org.amanzi.neo.data_generator.data.calls.CommandRow;
import org.amanzi.neo.data_generator.data.calls.Probe;
import org.amanzi.neo.data_generator.data.calls.ProbeData;
import org.amanzi.neo.data_generator.utils.call.CallConstants;
import org.amanzi.neo.data_generator.utils.call.CallGeneratorUtils;
import org.amanzi.neo.data_generator.utils.call.CommandCreator;

/**
 * <p>
 * Generator for GroupCallsData. 
 * </p>
 * @author Shcharbatsevich_A
 * @since 1.0.0
 */
public class GroupCallsGenerator extends CallDataGenerator{
    
    private static final String PAIR_DIRECTORY_POSTFIX = "GroupCall";
    
    private int maxGroupSize;

    /**
     * @param aDirectory
     * @param aHours
     * @param aHourDrift
     * @param aCallsPerHour
     * @param aCallPerHourVariance
     * @param aMaxGroupSize
     * @param aProbes
     */
    public GroupCallsGenerator(String aDirectory, Integer aHours, Integer aHourDrift, Integer aCallsPerHour,
            Integer aCallPerHourVariance, Integer aProbes, Integer aMaxGroupSize) {
        super(aDirectory, aHours, aHourDrift, aCallsPerHour, aCallPerHourVariance, aProbes);
        maxGroupSize = aMaxGroupSize;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected CallData buildCallCommands(CallGroup group,Integer hour, Call... calls) {
        Call call = calls[0];
        Long networkIdentity = getNetworkIdentity();
        List<Probe> probes = getProbes();
        Long startHour = getStartOfHour(hour);
        Long endHour = getStartOfHour(hour+1);
        Long start = call.getStartTime();
        Long setupDuration = (Long)call.getParameter(CallParameterNames.SETUP_TIME);
        Long callDuration = (Long)call.getParameter(CallParameterNames.DURATION_TIME);
        
        Integer sourceNum = group.getSourceProbe();
        List<Integer> receiverNums = group.getReceiverProbes();
        
        Long time = CallGeneratorUtils.getRamdomTime(startHour, start);
        ProbeData source = getNewProbeData(time, sourceNum);
        Probe sourceInfo = probes.get(sourceNum-1);
        List<CommandRow> sourceCommands = source.getCommands();
        
        int resCount = receiverNums.size();
        ProbeData[] receivers = new ProbeData[resCount];
        List<Probe> allReceiversInfo = new ArrayList<Probe>(resCount);
        List<List<CommandRow>> allReceiverCommands = new ArrayList<List<CommandRow>>(resCount);
        for(int i=0; i<resCount; i++){
            Integer receiverNum = receiverNums.get(i);
            time = CallGeneratorUtils.getRamdomTime(time, start);
            receivers[i] = getNewProbeData(time, receiverNum);
            allReceiversInfo.add(probes.get(receiverNum-1));
            allReceiverCommands.add(receivers[i].getCommands());
        }
        
        time = CallGeneratorUtils.getRamdomTime(time, start);
        sourceCommands.add(CommandCreator.getAtCtgsRow(time));
        CommandRow sourceCtgs = CommandCreator.getCtgsRow(sourceInfo.getSourceGroups(),sourceInfo.getResGroups());
        sourceCommands.add(CommandCreator.getAtCtgsRow(time,sourceCtgs));
        
        for(int i=0; i<resCount; i++){
            List<CommandRow> receiverCommands = allReceiverCommands.get(i);
            Probe resInfo = allReceiversInfo.get(i);
            time = CallGeneratorUtils.getRamdomTime(time, start);
            receiverCommands.add(CommandCreator.getAtCtgsRow(time));
            CommandRow receiverCtgs = CommandCreator.getCtgsRow(resInfo.getSourceGroups(),resInfo.getResGroups());
            receiverCommands.add(CommandCreator.getAtCtgsRow(time,receiverCtgs));
        }
        
        time = CallGeneratorUtils.getRamdomTime(time, start);
        sourceCommands.add(CommandCreator.getAtCciRow(time));
        CommandRow sourceCci = CommandCreator.getCciRow(networkIdentity,sourceInfo.getLocalAria(),sourceInfo.getFrequency());
        sourceCommands.add(CommandCreator.getAtCciRow(time,sourceCci));
        
        for(int i=0; i<resCount; i++){
            List<CommandRow> receiverCommands = allReceiverCommands.get(i);
            Probe receiverInfo = allReceiversInfo.get(i);
            time = CallGeneratorUtils.getRamdomTime(time, start);
            receiverCommands.add(CommandCreator.getAtCciRow(time));
            CommandRow receiverCci = CommandCreator.getCciRow(networkIdentity,receiverInfo.getLocalAria(),receiverInfo.getFrequency());
            receiverCommands.add(CommandCreator.getAtCciRow(time,receiverCci));
        }
        
        CommandRow ctsdcRow = CommandCreator.getCtsdcRow(start,0,0,0,1,1,0,1,1,0,0);
        sourceCommands.add(ctsdcRow);        
        time = CallGeneratorUtils.getRamdomTime(0L, setupDuration);
        sourceCommands.add(CommandCreator.getCtsdcRow(start+time,ctsdcRow));
        
        time = CallGeneratorUtils.getRamdomTime(time, setupDuration);
        CommandRow atdRow = CommandCreator.getAtdRow(start+time, group.getGroupNumber());
        sourceCommands.add(atdRow);
        
        Long end = start+setupDuration;
        CommandRow ctcc1 = CommandCreator.getCtccRow(end, 2,1,1,0,0,1,1);
        
        Long rest = callDuration-setupDuration;        
        
        CommandRow ctcc2 = CommandCreator.getCtccRow(null, 2,1,1,0,0,1,1);
        String numKey = networkIdentity+"0"+sourceInfo.getPhoneNumber();
        CommandRow ctxg = CommandCreator.getCtxgRow(numKey,2,3,0,0,1);
        
        time = CallGeneratorUtils.getRamdomTime(0L, rest);
        for(int i=0; i<resCount; i++){
            List<CommandRow> receiverCommands = allReceiverCommands.get(i);
            Long time1 = CallGeneratorUtils.getRamdomTime(0L, time);
            receiverCommands.add(CommandCreator.getCticnRow(end+time1, numKey, ctcc2, ctxg));
        }
        
        sourceCommands.add(CommandCreator.getAtdRow(end+time, atdRow, ctcc1, CommandCreator.getCtxgRow(2,0,0,0)));
        
        time = CallGeneratorUtils.getRamdomTime(0L, rest);
        sourceCommands.add(CommandCreator.getAthRow(end+time));
        time = CallGeneratorUtils.getRamdomTime(time, rest);
        CommandRow ctcrRow = CommandCreator.getCtcrRow(end+time,2,1);
        
        end+=rest;
        sourceCommands.add(CommandCreator.getAthRow(end,ctcrRow));
        rest = endHour-end;
        if(rest<0){
            rest = CallGeneratorUtils.HOUR;
        }
        time = CallGeneratorUtils.getRamdomTime(0L, rest);
        for(int i=0; i<resCount; i++){
            List<CommandRow> receiverCommands = allReceiverCommands.get(i);
            time = CallGeneratorUtils.getRamdomTime(time, rest);
            ctcrRow = CommandCreator.getCtcrRow(null,2,14);
            receiverCommands.add(CommandCreator.getUnsoCtcrRow(end+time,ctcrRow));
        }      
        
        Long time1 = time;
        List<Float> audioQuals = (List<Float>)call.getParameter(CallParameterNames.AUDIO_QUALITY+group.getSourceName());
        for(Float quality : audioQuals){
            sourceCommands.add(CommandCreator.getPESQRow(end+time1,quality));
            time1 = CallGeneratorUtils.getRamdomTime(time1, rest);
        }
        time1 = time;
        List<String> receiverNames = group.getReceiverNames();
        for (int j=0; j<resCount; j++) {
            audioQuals = (List<Float>)call.getParameter(CallParameterNames.AUDIO_QUALITY + receiverNames.get(j));
            List<CommandRow> receiverCommands = allReceiverCommands.get(j);
            for (Float quality : audioQuals) {
                receiverCommands.add(CommandCreator.getPESQRow(end+time1, quality));
                time1 = CallGeneratorUtils.getRamdomTime(time1, rest);
            }
        }
        //TODO priority
        CallData callData = new CallData(getKey(),source, receivers);
        callData.addCall(call);
        return callData;
    }
    

    @Override
    protected String getTypeKey() {
        return PAIR_DIRECTORY_POSTFIX;
    }

    @Override
    protected List<CallGroup> initCallGroups() {
        List<CallGroup> result = new ArrayList<CallGroup>();
        int groupSize = 1;
        while (groupSize<maxGroupSize) {
            groupSize++;            
            List<List<Integer>> groups = CallGeneratorUtils.buildAllGroups(groupSize, getProbesCount());
            for(List<Integer> group : groups){
                boolean canBeGroup = getRandomGenerator().getBooleanValue();
                if(canBeGroup){
                    int source = group.get(0);
                    CallGroup call = getCallGroup(source, group.subList(1, groupSize).toArray(new Integer[0]));
                    result.add(call);
                }
            }
            
        }
        return result;
    }

    @Override
    protected float[] getCallDurationBorders() {
        return CallConstants.GR_CALL_DURATION_BORDERS;
    }

    @Override
    protected float[] getAudioQualityBorders() {
        return CallConstants.GR_AUDIO_QUAL_BORDERS;
    }

    @Override
    protected Long getMinCallDuration() {
        return (long)(CallConstants.GR_CALL_DURATION_TIME*CallGeneratorUtils.MILLISECONDS);
    }

    @Override
    protected Integer getCallPriority() {
        return 0; //TODO correct.
    }
}