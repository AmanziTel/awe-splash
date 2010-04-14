
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
    @ASN1Sequence ( name = "UE_Positioning_GPS_MeasurementResults", isSet = false )
    public class UE_Positioning_GPS_MeasurementResults implements IASN1PreparedElement {
            
        
    @ASN1PreparedElement
    @ASN1Choice ( name = "referenceTime" )
    public static class ReferenceTimeChoiceType implements IASN1PreparedElement {
            
        @ASN1Element ( name = "utran-GPSReferenceTimeResult", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private UTRAN_GPSReferenceTimeResult utran_GPSReferenceTimeResult = null;
                
  @ASN1Integer( name = "" )
    @ASN1ValueRangeConstraint ( 
		
		min = 0L, 
		
		max = 604799999L 
		
	   )
	   
        @ASN1Element ( name = "gps-ReferenceTimeOnly", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private Integer gps_ReferenceTimeOnly = null;
                
  
        
        public UTRAN_GPSReferenceTimeResult getUtran_GPSReferenceTimeResult () {
            return this.utran_GPSReferenceTimeResult;
        }

        public boolean isUtran_GPSReferenceTimeResultSelected () {
            return this.utran_GPSReferenceTimeResult != null;
        }

        private void setUtran_GPSReferenceTimeResult (UTRAN_GPSReferenceTimeResult value) {
            this.utran_GPSReferenceTimeResult = value;
        }

        
        public void selectUtran_GPSReferenceTimeResult (UTRAN_GPSReferenceTimeResult value) {
            this.utran_GPSReferenceTimeResult = value;
            
                    setUtran_GPSReferenceTimeResult(null);
                
                    setGps_ReferenceTimeOnly(null);
                            
        }

        
  
        
        public Integer getGps_ReferenceTimeOnly () {
            return this.gps_ReferenceTimeOnly;
        }

        public boolean isGps_ReferenceTimeOnlySelected () {
            return this.gps_ReferenceTimeOnly != null;
        }

        private void setGps_ReferenceTimeOnly (Integer value) {
            this.gps_ReferenceTimeOnly = value;
        }

        
        public void selectGps_ReferenceTimeOnly (Integer value) {
            this.gps_ReferenceTimeOnly = value;
            
                    setUtran_GPSReferenceTimeResult(null);
                
                    setGps_ReferenceTimeOnly(null);
                            
        }

        
  

	    public void initWithDefaults() {
	    }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_ReferenceTimeChoiceType;
        }

        private static IASN1PreparedElementData preparedData_ReferenceTimeChoiceType = CoderFactory.getInstance().newPreparedElementData(ReferenceTimeChoiceType.class);

    }

                
        @ASN1Element ( name = "referenceTime", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private ReferenceTimeChoiceType referenceTime = null;
                
  
        @ASN1Element ( name = "gps-MeasurementParamList", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private GPS_MeasurementParamList gps_MeasurementParamList = null;
                
  
        
        public ReferenceTimeChoiceType getReferenceTime () {
            return this.referenceTime;
        }

        

        public void setReferenceTime (ReferenceTimeChoiceType value) {
            this.referenceTime = value;
        }
        
  
        
        public GPS_MeasurementParamList getGps_MeasurementParamList () {
            return this.gps_MeasurementParamList;
        }

        

        public void setGps_MeasurementParamList (GPS_MeasurementParamList value) {
            this.gps_MeasurementParamList = value;
        }
        
  
                    
        
        public void initWithDefaults() {
            
        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(UE_Positioning_GPS_MeasurementResults.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

            
    }
            