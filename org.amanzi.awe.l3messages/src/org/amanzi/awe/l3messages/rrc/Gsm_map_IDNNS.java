
package org.amanzi.awe.l3messages.rrc;
//
// This file was generated by the BinaryNotes compiler.
// See http://bnotes.sourceforge.net 
// Any modifications to this file will be lost upon recompilation of the source ASN.1. 
//

import org.bn.*;
import org.bn.annotations.*;
import org.bn.annotations.constraints.*;
import org.bn.coders.*;
import org.bn.types.*;




    @ASN1PreparedElement
    @ASN1Sequence ( name = "Gsm_map_IDNNS", isSet = false )
    public class Gsm_map_IDNNS implements IASN1PreparedElement {
            
        
    @ASN1PreparedElement
    @ASN1Choice ( name = "routingbasis" )
    public static class RoutingbasisChoiceType implements IASN1PreparedElement {
            

       @ASN1PreparedElement
       @ASN1Sequence ( name = "localPTMSI" , isSet = false )
       public static class LocalPTMSISequenceType implements IASN1PreparedElement {
                
        @ASN1Element ( name = "routingparameter", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private RoutingParameter routingparameter = null;
                
  
        
        public RoutingParameter getRoutingparameter () {
            return this.routingparameter;
        }

        

        public void setRoutingparameter (RoutingParameter value) {
            this.routingparameter = value;
        }
        
  
                
                
        public void initWithDefaults() {
            
        }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_LocalPTMSISequenceType;
        }

       private static IASN1PreparedElementData preparedData_LocalPTMSISequenceType = CoderFactory.getInstance().newPreparedElementData(LocalPTMSISequenceType.class);
                
       }

       
                
        @ASN1Element ( name = "localPTMSI", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private LocalPTMSISequenceType localPTMSI = null;
                
  

       @ASN1PreparedElement
       @ASN1Sequence ( name = "tMSIofsamePLMN" , isSet = false )
       public static class TMSIofsamePLMNSequenceType implements IASN1PreparedElement {
                
        @ASN1Element ( name = "routingparameter", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private RoutingParameter routingparameter = null;
                
  
        
        public RoutingParameter getRoutingparameter () {
            return this.routingparameter;
        }

        

        public void setRoutingparameter (RoutingParameter value) {
            this.routingparameter = value;
        }
        
  
                
                
        public void initWithDefaults() {
            
        }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_TMSIofsamePLMNSequenceType;
        }

       private static IASN1PreparedElementData preparedData_TMSIofsamePLMNSequenceType = CoderFactory.getInstance().newPreparedElementData(TMSIofsamePLMNSequenceType.class);
                
       }

       
                
        @ASN1Element ( name = "tMSIofsamePLMN", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private TMSIofsamePLMNSequenceType tMSIofsamePLMN = null;
                
  

       @ASN1PreparedElement
       @ASN1Sequence ( name = "tMSIofdifferentPLMN" , isSet = false )
       public static class TMSIofdifferentPLMNSequenceType implements IASN1PreparedElement {
                
        @ASN1Element ( name = "routingparameter", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private RoutingParameter routingparameter = null;
                
  
        
        public RoutingParameter getRoutingparameter () {
            return this.routingparameter;
        }

        

        public void setRoutingparameter (RoutingParameter value) {
            this.routingparameter = value;
        }
        
  
                
                
        public void initWithDefaults() {
            
        }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_TMSIofdifferentPLMNSequenceType;
        }

       private static IASN1PreparedElementData preparedData_TMSIofdifferentPLMNSequenceType = CoderFactory.getInstance().newPreparedElementData(TMSIofdifferentPLMNSequenceType.class);
                
       }

       
                
        @ASN1Element ( name = "tMSIofdifferentPLMN", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private TMSIofdifferentPLMNSequenceType tMSIofdifferentPLMN = null;
                
  

       @ASN1PreparedElement
       @ASN1Sequence ( name = "iMSIresponsetopaging" , isSet = false )
       public static class IMSIresponsetopagingSequenceType implements IASN1PreparedElement {
                
        @ASN1Element ( name = "routingparameter", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private RoutingParameter routingparameter = null;
                
  
        
        public RoutingParameter getRoutingparameter () {
            return this.routingparameter;
        }

        

        public void setRoutingparameter (RoutingParameter value) {
            this.routingparameter = value;
        }
        
  
                
                
        public void initWithDefaults() {
            
        }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_IMSIresponsetopagingSequenceType;
        }

       private static IASN1PreparedElementData preparedData_IMSIresponsetopagingSequenceType = CoderFactory.getInstance().newPreparedElementData(IMSIresponsetopagingSequenceType.class);
                
       }

       
                
        @ASN1Element ( name = "iMSIresponsetopaging", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private IMSIresponsetopagingSequenceType iMSIresponsetopaging = null;
                
  

       @ASN1PreparedElement
       @ASN1Sequence ( name = "iMSIcauseUEinitiatedEvent" , isSet = false )
       public static class IMSIcauseUEinitiatedEventSequenceType implements IASN1PreparedElement {
                
        @ASN1Element ( name = "routingparameter", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private RoutingParameter routingparameter = null;
                
  
        
        public RoutingParameter getRoutingparameter () {
            return this.routingparameter;
        }

        

        public void setRoutingparameter (RoutingParameter value) {
            this.routingparameter = value;
        }
        
  
                
                
        public void initWithDefaults() {
            
        }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_IMSIcauseUEinitiatedEventSequenceType;
        }

       private static IASN1PreparedElementData preparedData_IMSIcauseUEinitiatedEventSequenceType = CoderFactory.getInstance().newPreparedElementData(IMSIcauseUEinitiatedEventSequenceType.class);
                
       }

       
                
        @ASN1Element ( name = "iMSIcauseUEinitiatedEvent", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private IMSIcauseUEinitiatedEventSequenceType iMSIcauseUEinitiatedEvent = null;
                
  

       @ASN1PreparedElement
       @ASN1Sequence ( name = "iMEI" , isSet = false )
       public static class IMEISequenceType implements IASN1PreparedElement {
                
        @ASN1Element ( name = "routingparameter", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private RoutingParameter routingparameter = null;
                
  
        
        public RoutingParameter getRoutingparameter () {
            return this.routingparameter;
        }

        

        public void setRoutingparameter (RoutingParameter value) {
            this.routingparameter = value;
        }
        
  
                
                
        public void initWithDefaults() {
            
        }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_IMEISequenceType;
        }

       private static IASN1PreparedElementData preparedData_IMEISequenceType = CoderFactory.getInstance().newPreparedElementData(IMEISequenceType.class);
                
       }

       
                
        @ASN1Element ( name = "iMEI", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private IMEISequenceType iMEI = null;
                
  

       @ASN1PreparedElement
       @ASN1Sequence ( name = "spare2" , isSet = false )
       public static class Spare2SequenceType implements IASN1PreparedElement {
                
        @ASN1Element ( name = "routingparameter", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private RoutingParameter routingparameter = null;
                
  
        
        public RoutingParameter getRoutingparameter () {
            return this.routingparameter;
        }

        

        public void setRoutingparameter (RoutingParameter value) {
            this.routingparameter = value;
        }
        
  
                
                
        public void initWithDefaults() {
            
        }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_Spare2SequenceType;
        }

       private static IASN1PreparedElementData preparedData_Spare2SequenceType = CoderFactory.getInstance().newPreparedElementData(Spare2SequenceType.class);
                
       }

       
                
        @ASN1Element ( name = "spare2", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private Spare2SequenceType spare2 = null;
                
  

       @ASN1PreparedElement
       @ASN1Sequence ( name = "spare1" , isSet = false )
       public static class Spare1SequenceType implements IASN1PreparedElement {
                
        @ASN1Element ( name = "routingparameter", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private RoutingParameter routingparameter = null;
                
  
        
        public RoutingParameter getRoutingparameter () {
            return this.routingparameter;
        }

        

        public void setRoutingparameter (RoutingParameter value) {
            this.routingparameter = value;
        }
        
  
                
                
        public void initWithDefaults() {
            
        }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_Spare1SequenceType;
        }

       private static IASN1PreparedElementData preparedData_Spare1SequenceType = CoderFactory.getInstance().newPreparedElementData(Spare1SequenceType.class);
                
       }

       
                
        @ASN1Element ( name = "spare1", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private Spare1SequenceType spare1 = null;
                
  
        
        public LocalPTMSISequenceType getLocalPTMSI () {
            return this.localPTMSI;
        }

        public boolean isLocalPTMSISelected () {
            return this.localPTMSI != null;
        }

        private void setLocalPTMSI (LocalPTMSISequenceType value) {
            this.localPTMSI = value;
        }

        
        public void selectLocalPTMSI (LocalPTMSISequenceType value) {
            this.localPTMSI = value;
            
                    setTMSIofsamePLMN(null);
                
                    setTMSIofdifferentPLMN(null);
                
                    setIMSIresponsetopaging(null);
                
                    setIMSIcauseUEinitiatedEvent(null);
                
                    setIMEI(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public TMSIofsamePLMNSequenceType getTMSIofsamePLMN () {
            return this.tMSIofsamePLMN;
        }

        public boolean isTMSIofsamePLMNSelected () {
            return this.tMSIofsamePLMN != null;
        }

        private void setTMSIofsamePLMN (TMSIofsamePLMNSequenceType value) {
            this.tMSIofsamePLMN = value;
        }

        
        public void selectTMSIofsamePLMN (TMSIofsamePLMNSequenceType value) {
            this.tMSIofsamePLMN = value;
            
                    setLocalPTMSI(null);
                
                    setTMSIofdifferentPLMN(null);
                
                    setIMSIresponsetopaging(null);
                
                    setIMSIcauseUEinitiatedEvent(null);
                
                    setIMEI(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public TMSIofdifferentPLMNSequenceType getTMSIofdifferentPLMN () {
            return this.tMSIofdifferentPLMN;
        }

        public boolean isTMSIofdifferentPLMNSelected () {
            return this.tMSIofdifferentPLMN != null;
        }

        private void setTMSIofdifferentPLMN (TMSIofdifferentPLMNSequenceType value) {
            this.tMSIofdifferentPLMN = value;
        }

        
        public void selectTMSIofdifferentPLMN (TMSIofdifferentPLMNSequenceType value) {
            this.tMSIofdifferentPLMN = value;
            
                    setLocalPTMSI(null);
                
                    setTMSIofsamePLMN(null);
                
                    setIMSIresponsetopaging(null);
                
                    setIMSIcauseUEinitiatedEvent(null);
                
                    setIMEI(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public IMSIresponsetopagingSequenceType getIMSIresponsetopaging () {
            return this.iMSIresponsetopaging;
        }

        public boolean isIMSIresponsetopagingSelected () {
            return this.iMSIresponsetopaging != null;
        }

        private void setIMSIresponsetopaging (IMSIresponsetopagingSequenceType value) {
            this.iMSIresponsetopaging = value;
        }

        
        public void selectIMSIresponsetopaging (IMSIresponsetopagingSequenceType value) {
            this.iMSIresponsetopaging = value;
            
                    setLocalPTMSI(null);
                
                    setTMSIofsamePLMN(null);
                
                    setTMSIofdifferentPLMN(null);
                
                    setIMSIcauseUEinitiatedEvent(null);
                
                    setIMEI(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public IMSIcauseUEinitiatedEventSequenceType getIMSIcauseUEinitiatedEvent () {
            return this.iMSIcauseUEinitiatedEvent;
        }

        public boolean isIMSIcauseUEinitiatedEventSelected () {
            return this.iMSIcauseUEinitiatedEvent != null;
        }

        private void setIMSIcauseUEinitiatedEvent (IMSIcauseUEinitiatedEventSequenceType value) {
            this.iMSIcauseUEinitiatedEvent = value;
        }

        
        public void selectIMSIcauseUEinitiatedEvent (IMSIcauseUEinitiatedEventSequenceType value) {
            this.iMSIcauseUEinitiatedEvent = value;
            
                    setLocalPTMSI(null);
                
                    setTMSIofsamePLMN(null);
                
                    setTMSIofdifferentPLMN(null);
                
                    setIMSIresponsetopaging(null);
                
                    setIMEI(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public IMEISequenceType getIMEI () {
            return this.iMEI;
        }

        public boolean isIMEISelected () {
            return this.iMEI != null;
        }

        private void setIMEI (IMEISequenceType value) {
            this.iMEI = value;
        }

        
        public void selectIMEI (IMEISequenceType value) {
            this.iMEI = value;
            
                    setLocalPTMSI(null);
                
                    setTMSIofsamePLMN(null);
                
                    setTMSIofdifferentPLMN(null);
                
                    setIMSIresponsetopaging(null);
                
                    setIMSIcauseUEinitiatedEvent(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public Spare2SequenceType getSpare2 () {
            return this.spare2;
        }

        public boolean isSpare2Selected () {
            return this.spare2 != null;
        }

        private void setSpare2 (Spare2SequenceType value) {
            this.spare2 = value;
        }

        
        public void selectSpare2 (Spare2SequenceType value) {
            this.spare2 = value;
            
                    setLocalPTMSI(null);
                
                    setTMSIofsamePLMN(null);
                
                    setTMSIofdifferentPLMN(null);
                
                    setIMSIresponsetopaging(null);
                
                    setIMSIcauseUEinitiatedEvent(null);
                
                    setIMEI(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public Spare1SequenceType getSpare1 () {
            return this.spare1;
        }

        public boolean isSpare1Selected () {
            return this.spare1 != null;
        }

        private void setSpare1 (Spare1SequenceType value) {
            this.spare1 = value;
        }

        
        public void selectSpare1 (Spare1SequenceType value) {
            this.spare1 = value;
            
                    setLocalPTMSI(null);
                
                    setTMSIofsamePLMN(null);
                
                    setTMSIofdifferentPLMN(null);
                
                    setIMSIresponsetopaging(null);
                
                    setIMSIcauseUEinitiatedEvent(null);
                
                    setIMEI(null);
                
                    setSpare2(null);
                            
        }

        
  

	    public void initWithDefaults() {
	    }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_RoutingbasisChoiceType;
        }

        private static IASN1PreparedElementData preparedData_RoutingbasisChoiceType = CoderFactory.getInstance().newPreparedElementData(RoutingbasisChoiceType.class);

    }

                
        @ASN1Element ( name = "routingbasis", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private RoutingbasisChoiceType routingbasis = null;
                
  @ASN1Boolean( name = "" )
    
        @ASN1Element ( name = "dummy", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private Boolean dummy = null;
                
  
        
        public RoutingbasisChoiceType getRoutingbasis () {
            return this.routingbasis;
        }

        

        public void setRoutingbasis (RoutingbasisChoiceType value) {
            this.routingbasis = value;
        }
        
  
        
        public Boolean getDummy () {
            return this.dummy;
        }

        

        public void setDummy (Boolean value) {
            this.dummy = value;
        }
        
  
                    
        
        public void initWithDefaults() {
            
        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(Gsm_map_IDNNS.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

            
    }
            