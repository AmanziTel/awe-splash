
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
    @ASN1Enum (
        name = "EventIDInterRAT"
    )
    public class EventIDInterRAT implements IASN1PreparedElement {        
        public enum EnumType {
            
            @ASN1EnumItem ( name = "e3a", hasTag = true , tag = 0 )
            e3a , 
            @ASN1EnumItem ( name = "e3b", hasTag = true , tag = 1 )
            e3b , 
            @ASN1EnumItem ( name = "e3c", hasTag = true , tag = 2 )
            e3c , 
            @ASN1EnumItem ( name = "e3d", hasTag = true , tag = 3 )
            e3d , 
        }
        
        private EnumType value;
        private Integer integerForm;
        
        public EnumType getValue() {
            return this.value;
        }
        
        public void setValue(EnumType value) {
            this.value = value;
        }
        
        public Integer getIntegerForm() {
            return integerForm;
        }
        
        public void setIntegerForm(Integer value) {
            integerForm = value;
        }

	    public void initWithDefaults() {
	    }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(EventIDInterRAT.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
            