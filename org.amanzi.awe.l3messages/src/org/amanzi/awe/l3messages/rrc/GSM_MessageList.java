
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
    @ASN1BoxedType ( name = "GSM_MessageList" )
    public class GSM_MessageList implements IASN1PreparedElement {
                
            @ASN1ValueRangeConstraint ( 
		
		min = 1L, 
		
		max = 4L 
		
	   )
	   
            @ASN1SequenceOf( name = "GSM-MessageList" , isSetOf = false)
	    private java.util.Collection<GSM_MessageListEntry> value = null; 
    
            public GSM_MessageList () {
            }
        
            public GSM_MessageList ( java.util.Collection<GSM_MessageListEntry> value ) {
                setValue(value);
            }
                        
            public void setValue(java.util.Collection<GSM_MessageListEntry> value) {
                this.value = value;
            }
            
            public java.util.Collection<GSM_MessageListEntry> getValue() {
                return this.value;
            }            
            
            public void initValue() {
                setValue(new java.util.LinkedList<GSM_MessageListEntry>()); 
            }
            
            public void add(GSM_MessageListEntry item) {
                value.add(item);
            }

	    public void initWithDefaults() {
	    }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(GSM_MessageList.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
            