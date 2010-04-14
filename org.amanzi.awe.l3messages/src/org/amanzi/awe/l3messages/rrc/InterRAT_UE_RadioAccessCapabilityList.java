
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
    @ASN1BoxedType ( name = "InterRAT_UE_RadioAccessCapabilityList" )
    public class InterRAT_UE_RadioAccessCapabilityList implements IASN1PreparedElement {
                
            @ASN1ValueRangeConstraint ( 
		
		min = 1L, 
		
		max = 4L 
		
	   )
	   
            @ASN1SequenceOf( name = "InterRAT-UE-RadioAccessCapabilityList" , isSetOf = false)
	    private java.util.Collection<InterRAT_UE_RadioAccessCapability> value = null; 
    
            public InterRAT_UE_RadioAccessCapabilityList () {
            }
        
            public InterRAT_UE_RadioAccessCapabilityList ( java.util.Collection<InterRAT_UE_RadioAccessCapability> value ) {
                setValue(value);
            }
                        
            public void setValue(java.util.Collection<InterRAT_UE_RadioAccessCapability> value) {
                this.value = value;
            }
            
            public java.util.Collection<InterRAT_UE_RadioAccessCapability> getValue() {
                return this.value;
            }            
            
            public void initValue() {
                setValue(new java.util.LinkedList<InterRAT_UE_RadioAccessCapability>()); 
            }
            
            public void add(InterRAT_UE_RadioAccessCapability item) {
                value.add(item);
            }

	    public void initWithDefaults() {
	    }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(InterRAT_UE_RadioAccessCapabilityList.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
            