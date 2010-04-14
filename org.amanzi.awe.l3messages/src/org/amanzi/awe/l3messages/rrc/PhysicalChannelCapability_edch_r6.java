
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
    @ASN1Sequence ( name = "PhysicalChannelCapability_edch_r6", isSet = false )
    public class PhysicalChannelCapability_edch_r6 implements IASN1PreparedElement {
            
        
    @ASN1PreparedElement
    @ASN1Choice ( name = "fdd_edch" )
    public static class Fdd_edchChoiceType implements IASN1PreparedElement {
            

       @ASN1PreparedElement
       @ASN1Sequence ( name = "supported" , isSet = false )
       public static class SupportedSequenceType implements IASN1PreparedElement {
                @ASN1Integer( name = "" )
    @ASN1ValueRangeConstraint ( 
		
		min = 1L, 
		
		max = 16L 
		
	   )
	   
        @ASN1Element ( name = "edch-PhysicalLayerCategory", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private Integer edch_PhysicalLayerCategory = null;
                
  
        
        public Integer getEdch_PhysicalLayerCategory () {
            return this.edch_PhysicalLayerCategory;
        }

        

        public void setEdch_PhysicalLayerCategory (Integer value) {
            this.edch_PhysicalLayerCategory = value;
        }
        
  
                
                
        public void initWithDefaults() {
            
        }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_SupportedSequenceType;
        }

       private static IASN1PreparedElementData preparedData_SupportedSequenceType = CoderFactory.getInstance().newPreparedElementData(SupportedSequenceType.class);
                
       }

       
                
        @ASN1Element ( name = "supported", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private SupportedSequenceType supported = null;
                
  
        @ASN1Null ( name = "unsupported" ) 
    
        @ASN1Element ( name = "unsupported", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private org.bn.types.NullObject unsupported = null;
                
  
        
        public SupportedSequenceType getSupported () {
            return this.supported;
        }

        public boolean isSupportedSelected () {
            return this.supported != null;
        }

        private void setSupported (SupportedSequenceType value) {
            this.supported = value;
        }

        
        public void selectSupported (SupportedSequenceType value) {
            this.supported = value;
            
                    setUnsupported(null);
                            
        }

        
  
        
        public org.bn.types.NullObject getUnsupported () {
            return this.unsupported;
        }

        public boolean isUnsupportedSelected () {
            return this.unsupported != null;
        }

        private void setUnsupported (org.bn.types.NullObject value) {
            this.unsupported = value;
        }

        
        public void selectUnsupported () {
            selectUnsupported (new org.bn.types.NullObject());
	}
	
        public void selectUnsupported (org.bn.types.NullObject value) {
            this.unsupported = value;
            
                    setSupported(null);
                            
        }

        
  

	    public void initWithDefaults() {
	    }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_Fdd_edchChoiceType;
        }

        private static IASN1PreparedElementData preparedData_Fdd_edchChoiceType = CoderFactory.getInstance().newPreparedElementData(Fdd_edchChoiceType.class);

    }

                
        @ASN1Element ( name = "fdd_edch", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private Fdd_edchChoiceType fdd_edch = null;
                
  
        
        public Fdd_edchChoiceType getFdd_edch () {
            return this.fdd_edch;
        }

        

        public void setFdd_edch (Fdd_edchChoiceType value) {
            this.fdd_edch = value;
        }
        
  
                    
        
        public void initWithDefaults() {
            
        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(PhysicalChannelCapability_edch_r6.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

            
    }
            