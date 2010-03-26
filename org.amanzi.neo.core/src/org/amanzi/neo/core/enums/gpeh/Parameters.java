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

package org.amanzi.neo.core.enums.gpeh;

/**
 * <p>
 * GPEH parameters
 * </p>
 * 
 * @author Cinkel_A
 * @since 1.0.0
 */
public enum Parameters {
    EVENT_PARAM_ACT_REASON(4),
    EVENT_PARAM_ACTION(7),
    EVENT_PARAM_ASE_REDUCTION(17),
    EVENT_PARAM_NO_OF_CELLS_IN_ACTIVE_SET(3),
    EVENT_PARAM_NO_OF_CELLS_IN_REPORT(3),
    EVENT_PARAM_CHANNEL_SWITCH_REASON(5),
    EVENT_PARAM_CHANNELIZATION_CODE_NORM_MODE(9),
    EVENT_PARAM_CHANNELIZATION_CODE_COMP_MODE(9),
    EVENT_PARAM_CM_TYPE(3),
    EVENT_PARAM_DEACT_REASON(4),
    EVENT_PARAM_DL_POWER(11),
    EVENT_PARAM_EVENT_TRIGGER(8),
    EVENT_PARAM_FAILURE_REASON(8),
    EVENT_PARAM_CPICH_EC_NO_CELL_1(7),
    EVENT_PARAM_CPICH_EC_NO_CELL_2(7),
    EVENT_PARAM_CPICH_EC_NO_CELL_3(7),
    EVENT_PARAM_CPICH_EC_NO_CELL_4(7),
    EVENT_PARAM_CPICH_EC_NO_EVAL_CELL(7),
    EVENT_PARAM_RSCP_CELL_1(8),
    EVENT_PARAM_RSCP_CELL_2(8),
    EVENT_PARAM_RSCP_CELL_3(8),
    EVENT_PARAM_RSCP_CELL_4(8),
    EVENT_PARAM_RSCP_EVAL_CELL(8),
    EVENT_PARAM_SCRAMBLING_CODE_CELL_1(10),
    EVENT_PARAM_SCRAMBLING_CODE_CELL_2(10),
    EVENT_PARAM_SCRAMBLING_CODE_CELL_3(10),
    EVENT_PARAM_SCRAMBLING_CODE_CELL_4(10),
    EVENT_PARAM_SCRAMBLING_CODE_EVAL_CELL_1(10),
    EVENT_PARAM_SCRAMBLING_CODE_EVAL_CELL_2(10),
    EVENT_PARAM_SCRAMBLING_CODE_EVAL_CELL_3(10),
    EVENT_PARAM_SCRAMBLING_CODE_EVAL_CELL_4(10),
    //TODO check parsing parameter EVENT_PARAM_IMSI
    EVENT_PARAM_IMSI(60,Rules.LONG),
    EVENT_PARAM_MEAS_REPORTS_IN_BUFFER_1A_1C(5),
    EVENT_PARAM_MEAS_REPORTS_IN_BUFFER_1B(5),
    EVENT_PARAM_MEAS_REPORTS_IN_BUFFER_1D(5),
    EVENT_PARAM_MEAS_REPORTS_IN_BUFFER_3A(5),
    EVENT_PARAM_MEASURED_ENTITY(4),
    EVENT_PARAM_MEASURED_VALUE(11),
    EVENT_PARAM_NR_OF_CONNECTIONS_SF8(8),
    EVENT_PARAM_NR_OF_CONNECTIONS_SF16(8),
    EVENT_PARAM_NR_OF_CONNECTIONS_SF32(8),
    EVENT_PARAM_RESULT(3),
    EVENT_PARAM_RLC_BUFFER_VOLUME(16),
    EVENT_PARAM_URA_ID(17),
    EVENT_PARAM_CELL_UPDATE_CAUSE_VALUE(4),
    EVENT_PARAM_STATE_CHANGE_NODE(3),
    EVENT_PARAM_TOTAL_ASE_DL(17),
    EVENT_PARAM_TOTAL_ASE_UL(17),
    EVENT_PARAM_TOTAL_DL_CHAN_CODE_TREE_CONSUMPTION(16),
    EVENT_PARAM_UE_CONTEXT(16),
    EVENT_PARAM_UL_INTERFERENCE(11),
    EVENT_PARAM_UL_SIR_TARGET(9),
    EVENT_PARAM_VALUE_AT_FAILURE(24),
    EVENT_PARAM_RNC_MODULE_ID(7),
    EVENT_PARAM_EVENT_ID(11),
    EVENT_PARAM_TIMESTAMP_HOUR(5){
        @Override
        public boolean firstBitIsError() {
            return false;
        }
    },
    EVENT_PARAM_TIMESTAMP_MINUTE(6){
        @Override
        public boolean firstBitIsError() {
            return false;
        }
    },
    EVENT_PARAM_TIMESTAMP_SECOND(6){
        @Override
        public boolean firstBitIsError() {
            return false;
        }
    },
    EVENT_PARAM_TIMESTAMP_MILLISEC(11){
        @Override
        public boolean firstBitIsError() {
            return false;
        }
    },
    EVENT_PARAM_ERROR_TYPE(6),
    EVENT_PARAM_PROTOCOL_ERROR_CAUSE(16),
    EVENT_PARAM_RRC_TRANSACTION_IDENTIFIER(16),
    EVENT_PARAM_PROCEDURE_CODE(16),
    EVENT_PARAM_RECEIVED_MESSAGE_TYPE(16),
    EVENT_PARAM_IE_NAME(17),
    EVENT_PARAM_ERROR_LOCATION(256,Rules.STRING),
    EVENT_PARAM_ASN1_AVAILABILITY(2),
    EVENT_PARAM_MESSAGE_LENGTH(12),
    EVENT_PARAM_MESSAGE_DISCRIMINATOR(16),
    EVENT_PARAM_CN_DOMAIN_INDICATOR(16),
    EVENT_PARAM_HANDOVER_TYPE(3),
    EVENT_PARAM_PDU_TYPE(5),
    EVENT_PARAM_MESSAGE_DIRECTION(2),
    EVENT_PARAM_MESSAGE_CONTENTS(16384,Rules.STRING),
    EVENT_PARAM_NO_OF_CM_USERS(8),
    EVENT_PARAM_REQUEST_TYPE(3),
    EVENT_PARAM_TRAFFIC_CLASS(2),
    EVENT_PARAM_TRAFFIC_CLASS_UL(2),
    EVENT_PARAM_C_ID_1(17),
    EVENT_PARAM_C_ID_2(17),
    EVENT_PARAM_C_ID_3(17),
    EVENT_PARAM_C_ID_4(17),
    EVENT_PARAM_C_ID_EVALUATED(17),
    EVENT_PARAM_RNC_ID_1(13),
    EVENT_PARAM_RNC_ID_2(13),
    EVENT_PARAM_RNC_ID_3(13),
    EVENT_PARAM_RNC_ID_4(13),
    EVENT_PARAM_RNC_ID_EVALUATED(13),
    EVENT_PARAM_GSM_C_I(17),
    EVENT_PARAM_GSM_LAC(25),
    EVENT_PARAM_GSM_PLMN_ID(17),
    EVENT_PARAM_GSM_RSSI(7),
    EVENT_PARAM_IRAT_CELL_ID_1(6),
    EVENT_PARAM_IRAT_CELL_ID_2(6),
    EVENT_PARAM_IRAT_CELL_ID_3(6),
    EVENT_PARAM_IRAT_CELL_ID_4(6),
    EVENT_PARAM_RLS_ID_1(6),
    EVENT_PARAM_RLS_ID_2(6),
    EVENT_PARAM_RLS_ID_3(6),
    EVENT_PARAM_RLS_ID_4(6),
    EVENT_PARAM_RLS_TAG_1(2),
    EVENT_PARAM_RLS_TAG_2(2),
    EVENT_PARAM_RLS_TAG_3(2),
    EVENT_PARAM_RLS_TAG_4(2),
    EVENT_PARAM_UL_SCRAMBLING_CODE(25),
    EVENT_PARAM_SPREADING_FACTOR_NORM_MODE(10),
    EVENT_PARAM_SPREADING_FACTOR_COMP_MODE(10),
    EVENT_PARAM_CHANGED_NODE_SPREADING_FACTOR(10),
    EVENT_PARAM_CHANGED_NODE_CODE_NUMBER(9),
    EVENT_PARAM_CODE_TREE_LOAD(10),
    EVENT_PARAM_SCRAMBLING_CODE_REP_CELL_1(10),
    EVENT_PARAM_UARFCN_DL_REP_CELL_1(16),
    EVENT_PARAM_UARFCN_UL_REP_CELL_1(16),
    EVENT_PARAM_SCRAMBLING_CODE_REP_CELL_2(10),
    EVENT_PARAM_UARFCN_DL_REP_CELL_2(16),
    EVENT_PARAM_UARFCN_UL_REP_CELL_2(16),
    EVENT_PARAM_SCRAMBLING_CODE_REP_CELL_3(10),
    EVENT_PARAM_UARFCN_DL_REP_CELL_3(16),
    EVENT_PARAM_UARFCN_UL_REP_CELL_3(16),
    EVENT_PARAM_SCRAMBLING_CODE_REP_CELL_4(10),
    EVENT_PARAM_UARFCN_DL_REP_CELL_4(16),
    EVENT_PARAM_UARFCN_UL_REP_CELL_4(16),
    EVENT_PARAM_MEAS_REPORTS_IN_BUFFER_2B(2),
    EVENT_PARAM_C_ID_NU_FREQ(17),
    EVENT_PARAM_RNC_ID_NU_FREQ(13),
    EVENT_PARAM_CPICH_EC_NU_FREQ_CELL(7),
    EVENT_PARAM_RSCP_NU_FREQ_CELL(8),
    EVENT_PARAM_RNC_MP_ID(7),
    EVENT_PARAM_SCRAMBLING_CODE_TRIGGER_CELL(10),
    EVENT_PARAM_CPICH_EC_NO_TRIGGER_CELL(7),
    EVENT_PARAM_RSCP_TRIGGER_CELL(8),
    EVENT_PARAM_SCRAMBLING_CODE_ADDITIONAL_CELL1(10),
    EVENT_PARAM_CPICH_EC_NO_ADDITIONAL_CELL1(7),
    EVENT_PARAM_RSCP_ADDITIONAL_CELL1(8),
    EVENT_PARAM_SCRAMBLING_CODE_ADDITIONAL_CELL2(10),
    EVENT_PARAM_CPICH_EC_NO_ADDITIONAL_CELL2(7),
    EVENT_PARAM_RSCP_ADDITIONAL_CELL2(8),
    EVENT_PARAM_C_ID_TRIGGER_CELL(17),
    EVENT_PARAM_RNC_ID_TRIGGER_CELL(13),
    EVENT_PARAM_UE_POS_METHOD(3),
    EVENT_PARAM_NR_GPS_SATELLITES(6),
    EVENT_PARAM_MEASUREMENT_TIME(10),
    EVENT_PARAM_REQUIRED_HORIZONTAL_ACCURACY(8),
    EVENT_PARAM_ACHIEVED_HORIZONTAL_ACCURACY(8),
    EVENT_PARAM_REQUIRED_VERTICAL_ACCURACY(8),
    EVENT_PARAM_ACHIEVED_VERTICAL_ACCURACY(9),
    EVENT_PARAM_UE_REFERENCE_POSITION(2),
    EVENT_PARAM_LATITUDE_SIGN(2),
    EVENT_PARAM_LATITUDE(24),
    EVENT_PARAM_LONGITUDE_SIGN(2),
    EVENT_PARAM_LONGITUDE(25),
    EVENT_PARAM_ALTITUDE_DIRECTION(2),
    EVENT_PARAM_ALTITUDE(16),
    EVENT_PARAM_UNCERTAINTY_SEMI_MAJOR(8),
    EVENT_PARAM_UNCERTAINTY_SEMI_MINOR(8),
    EVENT_PARAM_ORIENTATION_MAJOR_AXIS(9),
    EVENT_PARAM_CONFIDENCE(8),
    EVENT_PARAM_HS_DSCH_PHYSICAL_LAYER_CATEGORY(8),
    EVENT_PARAM_E_DCH_AND_OR_HS_DSCH_CELL_SELECTION_RESULT(4),    
    EVENT_PARAM_E_DCH_PHYSICAL_LAYER_CATEGORY(8),
    EVENT_PARAM_E_DCH_AND_OR_HS_DSCH_CELL_ID(17),
    EVENT_PARAM_REQUESTED_SERVICE_CLASS(2),
    EVENT_PARAM_REQUESTED_SETUP_CLASS(2),
    EVENT_PARAM_REQUESTED_ORIGIN(2),
    EVENT_PARAM_REQUESTED_SF_DL(10),
    EVENT_PARAM_REQUESTED_SF_UL(10),
    EVENT_PARAM_REQUESTED_COMPRESSED_MODE(2),
    EVENT_PARAM_REQUESTED_RAB_STATE(6),
    EVENT_PARAM_CURRENT_SF_DL(10),
    EVENT_PARAM_CURRENT_SF_UL(10),
    EVENT_PARAM_CURRENT_RAB_STATE(6),
    EVENT_PARAM_BLOCKING_PROCEDURE(4),
    EVENT_PARAM_BLOCK_REASON(6),
    EVENT_PARAM_BLOCK_TYPE(4),
    EVENT_PARAM_BLOCK_ADMISSION_POLICY_LEVEL(16),
    EVENT_PARAM_LOCAL_CELL_GROUP_ID(29),
    EVENT_PARAM_RBS_ID(32),
    EVENT_PARAM_CAPACITY_CREDITS_UL(17),
    EVENT_PARAM_CAPACITY_CREDITS_DL(17),
    EVENT_PARAM_CONSUMED_CREDITS_UL(18),
    EVENT_PARAM_CONSUMED_CREDITS_DL(18),
    EVENT_PARAM_C_ID_SOURCE(17),
    EVENT_PARAM_CPICH_EC_NO_SOURCE_CELL(7),
    EVENT_PARAM_CPICH_RSCP_SOURCE_CELL(8),
    EVENT_PARAM_C_ID_TARGET(17),
    EVENT_PARAM_CPICH_EC_NO_TARGET_CELL(7),
    EVENT_PARAM_CPICH_RSCP_TARGET_CELL(8),
    EVENT_PARAM_SELECTED_CELL(2),
    EVENT_PARAM_PATHLOSS(10),
    EVENT_PARAM_UARFCN_SOURCE(15),
    EVENT_PARAM_UARFCN_TARGET(15),
    EVENT_PARAM_UL_SYNC_STATUS_RLS1(2),
    EVENT_PARAM_NO_OF_EUL_USERS_TO_BE_SWITCHED(10),
    EVENT_PARAM_NUMBER_OF_USERS_ASSIGNED_TO_PHYS_HS_CHAN(10),
    EVENT_PARAM_NUMBER_OF_EDCH_USERS_IN_SERVING_CELL(8),
    EVENT_PARAM_NUMBER_OF_EDCH_USERS_IN_NON_SERVING_CELL(10),
    EVENT_PARAM_NR_OF_CONNECTIONS_SF4_UL(8),
    EVENT_PARAM_PROTOCOL_ID(4),
    EVENT_PARAM_UE_POS_UNSUCC_CAUSE(5),
    EVENT_PARAM_UARFCN_DL_NON_USED_FREQ_1(15),
    EVENT_PARAM_UARFCN_DL_NON_USED_FREQ_2(15),
    EVENT_PARAM_UARFCN_DL_NON_USED_FREQ_3(15),
    EVENT_PARAM_TRIGGER_POINT(3),
    EVENT_PARAM_UTRAN_RANAP_CAUSE(11),
    EVENT_PARAM_DATA_IN_DL_RLC_BUFFERS(6),    
    EVENT_PARAM_RAN_DISCONNECTION_CODE(6),
    EVENT_PARAM_RAN_DISCONNECTION_SUBCODE(5),
    EVENT_PARAM_RAN_ID_PARAMETER(17),
    EVENT_PARAM_CPM(3),
    EVENT_PARAM_SCRAMBLING_CODE_ADDED_CELL(10),
    EVENT_PARAM_CPICH_EC_NO_ADDED_CELL(7),
    EVENT_PARAM_RSCP_CELL_1_ADDED_CELL(8),
    EVENT_PARAM_CNHHO_TYPE(2),
    EVENT_PARAM_IUR_STATUS(2),
    EVENT_PARAM_UL_THROUGHPUT(15),
    EVENT_PARAM_DL_THROUGHPUT(15),
    EVENT_PARAM_NR_OF_CONNECTIONS_SF8_UL(8),
    EVENT_PARAM_NR_OF_CONNECTIONS_SF16_UL(8),
    EVENT_PARAM_SOURCE_CONF(8),
    EVENT_PARAM_TARGET_CONF(8),
    EVENT_PARAM_TARGET_CAP(2),
    EVENT_PARAM_UEPOS_INITIAL_CONF(8),
    EVENT_PARAM_UEPOS_FINAL_CONF(8),
    // not in bitpack
    EVENT_PARAM_MEASUREMENT_TYPE(1*8),
  // not in bitpack
    EVENT_PARAM_MEASUREMENT_LENGTH(2*8),
    EVENT_PARAM_UL_INT_CELL1(11),
    EVENT_PARAM_UL_INT_CELL2(11),
    EVENT_PARAM_UL_INT_CELL3(11),
    EVENT_PARAM_UL_INT_CELL4(11),
    EVENT_PARAM_C_ID_SERV_HSDSCH_CELL(17),
    EVENT_PARAM_CRNC_ID_SERV_HSDSCH_CELL(13),
    EVENT_PARAM_EC_NO_SERV_HSDSCH_CELL(7),
    EVENT_PARAM_RSCP_SERV_HSDSCH_CELL(8),
    EVENT_PARAM_AFFECTED_SCANNER_TYPE(3),
    EVENT_PARAM_EXCEPTION_CLASS(9),
    EVENT_PARAM_CAUSE_VALUE(9),
    EVENT_PARAM_EXTENDED_CAUSE_VALUE(9),
    EVENT_PARAM_SEVERITY_INDICATOR(2),
    EVENT_PARAM_CN_RANAP_CAUSE(11),
    EVENT_PARAM_ARP(5),
    EVENT_PARAM_PREEMPT_CAP(2),
    EVENT_PARAM_NO_OF_NGUAR_CONN_TARGETED(8),
    EVENT_PARAM_NO_OF_NGUAR_CONN_TARGETED_IUR(8),
    EVENT_PARAM_NO_OF_GUAR_CONN_TARGETED(8),
    EVENT_PARAM_NO_OF_GUAR_CONN_TARGETED_IUR(8),
    EVENT_PARAM_FILTERED_POWER(11),
    EVENT_PARAM_SCANNER_ID(24),
    EVENT_PARAM_RNC_ID_SOURCE(13),
    EVENT_PARAM_RNC_ID_TARGET(13),
    EVENT_PARAM_PREVIOUS_DOWNLINK_STATE(3), 
    EVENT_PARAM_CURRENT_DOWNLINK_STATE(3),
    EVENT_PARAM_PREVIOUS_UPLINK_STATE(2),
    EVENT_PARAM_CURRENT_UPLINK_STATE(2),
    EVENT_PARAM_GBR_UL(15),
    EVENT_PARAM_GBR_DL(15),
    EVENT_PARAM_UL_USER_THROUGHPUT(15),
    EVENT_PARAM_DL_USER_THROUGHPUT(15),
    EVENT_PARAM_TMGI_PLMN_ID(25),
    EVENT_PARAM_TMGI_SERVICE_ID(25),
    EVENT_PARAM_SESSION_ID(9),
    EVENT_PARAM_MBMS_CONF(4),
    EVENT_PARAM_NBAP_CAUSE(16),
    EVENT_PARAM_FIRST_START(2),
    EVENT_PARAM_SESSION_STATUS(3),
    EVENT_PARAM_UE_POS_CELLID_UNSUCC_CAUSE(3),
    EVENT_PARAM_UE_POS_RTT_UNSUCC_CAUSE(5),
    EVENT_PARAM_UE_POS_AGPS_UNSUCC_CAUSE(5),
    EVENT_PARAM_UE_POS_METHOD_1(3),
    EVENT_PARAM_UE_POS_METHOD_2(3),
    EVENT_PARAM_UE_POS_METHOD_3(3),
    EVENT_PARAM_REQUIRED_MEASUREMENT_TIME(18),
    EVENT_PARAM_UERXTX_TYPE1_MEASUREMENT(12),
    EVENT_PARAM_RTT_MEASUREMENT(16),
    EVENT_PARAM_EXTENDED_RANGE_RTT_MEASUREMENT(18),
    EVENT_PARAM_RTT_OVER_RNSAP(2),
    EVENT_PARAM_INNER_RADIUS(17),
    EVENT_PARAM_UNCERTAINTY_RADIUS(8), 
    EVENT_PARAM_OFFSET_ANGLE(10),
    EVENT_PARAM_INCLUDED_ANGLE(10),
    EVENT_PARAM_SHAPE_BEFORE_CONVERSION(4),
    EVENT_PARAM_SHAPE_AFTER_CONVERSION(4),
    EVENT_PARAM_POSITION_DATA(25),
    EVENT_PARAM_ACCURACY_FULFILMENT_INDICATOR(2),
    EVENT_PARAM_UEPOS_INITIAL_CONFIDENCE(8),
    EVENT_PARAM_UEPOS_FINAL_CONFIDENCE(8),
    EVENT_PARAM_ANTENNA_POSITION_LAT_SIGN(2),
    EVENT_PARAM_ANTENNA_POSITION_LAT_DEGREES(24),
    EVENT_PARAM_ANTENNA_POSITION_LONG_SIGN(2),
    EVENT_PARAM_ANTENNA_POSITION_LONG_DEGREES(25),   
    EVENT_PARAM_NUMBER_OF_POLYGON_CORNERS(5), 
    
    EVENT_PARAM_POLYGON_CORNER_1_LAT_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_2_LAT_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_3_LAT_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_4_LAT_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_5_LAT_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_6_LAT_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_7_LAT_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_8_LAT_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_9_LAT_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_10_LAT_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_11_LAT_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_12_LAT_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_13_LAT_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_14_LAT_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_15_LAT_SIGN(2),    
    EVENT_PARAM_POLYGON_CORNER_1_LAT_DEGREES(24),
    EVENT_PARAM_POLYGON_CORNER_2_LAT_DEGREES(24),
    EVENT_PARAM_POLYGON_CORNER_3_LAT_DEGREES(24),
    EVENT_PARAM_POLYGON_CORNER_4_LAT_DEGREES(24),
    EVENT_PARAM_POLYGON_CORNER_5_LAT_DEGREES(24),
    EVENT_PARAM_POLYGON_CORNER_6_LAT_DEGREES(24),
    EVENT_PARAM_POLYGON_CORNER_7_LAT_DEGREES(24),
    EVENT_PARAM_POLYGON_CORNER_8_LAT_DEGREES(24),
    EVENT_PARAM_POLYGON_CORNER_9_LAT_DEGREES(24),
    EVENT_PARAM_POLYGON_CORNER_10_LAT_DEGREES(24),
    EVENT_PARAM_POLYGON_CORNER_11_LAT_DEGREES(24),
    EVENT_PARAM_POLYGON_CORNER_12_LAT_DEGREES(24),
    EVENT_PARAM_POLYGON_CORNER_13_LAT_DEGREES(24),
    EVENT_PARAM_POLYGON_CORNER_14_LAT_DEGREES(24),
    EVENT_PARAM_POLYGON_CORNER_15_LAT_DEGREES(24),   
    EVENT_PARAM_POLYGON_CORNER_1_LONG_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_2_LONG_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_3_LONG_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_4_LONG_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_5_LONG_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_6_LONG_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_7_LONG_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_8_LONG_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_9_LONG_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_10_LONG_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_11_LONG_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_12_LONG_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_13_LONG_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_14_LONG_SIGN(2),
    EVENT_PARAM_POLYGON_CORNER_15_LONG_SIGN(2),    
    EVENT_PARAM_POLYGON_CORNER_1_LONG_DEGREES(25), 
    EVENT_PARAM_POLYGON_CORNER_2_LONG_DEGREES(25), 
    EVENT_PARAM_POLYGON_CORNER_3_LONG_DEGREES(25), 
    EVENT_PARAM_POLYGON_CORNER_4_LONG_DEGREES(25), 
    EVENT_PARAM_POLYGON_CORNER_5_LONG_DEGREES(25), 
    EVENT_PARAM_POLYGON_CORNER_6_LONG_DEGREES(25), 
    EVENT_PARAM_POLYGON_CORNER_7_LONG_DEGREES(25), 
    EVENT_PARAM_POLYGON_CORNER_8_LONG_DEGREES(25), 
    EVENT_PARAM_POLYGON_CORNER_9_LONG_DEGREES(25), 
    EVENT_PARAM_POLYGON_CORNER_10_LONG_DEGREES(25), 
    EVENT_PARAM_POLYGON_CORNER_11_LONG_DEGREES(25), 
    EVENT_PARAM_POLYGON_CORNER_12_LONG_DEGREES(25), 
    EVENT_PARAM_POLYGON_CORNER_13_LONG_DEGREES(25), 
    EVENT_PARAM_POLYGON_CORNER_14_LONG_DEGREES(25), 
    EVENT_PARAM_POLYGON_CORNER_15_LONG_DEGREES(25),    
    EVENT_PARAM_MAX_DL_POWER(11),
    EVENT_PARAM_DL_TX_POWER(11),
    EVENT_PARAM_DL_NON_HS_TX_POWER(11),
    EVENT_PARAM_DL_HS_REQ_POWER(11),
    EVENT_PARAM_NUMBER_OF_MCCH_MICH(2),
    EVENT_PARAM_NUMBER_OF_MTCH_64(5),
    EVENT_PARAM_NUMBER_OF_MTCH_128(5),
    EVENT_PARAM_NUMBER_OF_MTCH_256(5),
    EVENT_PARAM_UERXTX_TYPE2_MEASUREMENT(14),
    EVENT_PARAM_PCAP_ERROR_TYPE(4),
    EVENT_PARAM_SELECTED_POSITION_METHOD(4),
    EVENT_PARAM_ADDITIONAL_METHOD_TYPE(3),
    EVENT_PARAM_RNC_RESPONSE_TIME(5),
    EVENT_PARAM_UE_POS_CAPABILITY_STAND_ALONE(2),
    EVENT_PARAM_UE_POS_CAPABILITY_UE_BASED_OTDOA(2),
    EVENT_PARAM_UE_POS_CAPABILITY_NETWORK_AGPS(3),
    EVENT_PARAM_UE_POS_CAPABILITY_GPS_TIMING(2),
    EVENT_PARAM_UE_POS_CAPABILITY_IPDL(2),
    EVENT_PARAM_UE_POS_CAPABILITY_RXTX_TYPE2(2),
    EVENT_PARAM_UE_POS_CAPABILITY_GPS_PCH(2),
    EVENT_PARAM_UE_POS_CAPABILITY_SFNTYPE2(2),
    EVENT_PARAM_MESSAGE_SENT_WHEN_TIMER_EXPIRES(2),
    EVENT_PARAM_T_POS_SAS_EXPIRED(2),
    EVENT_PARAM_AMOUNT_OF_REPORTING(4),
    EVENT_PARAM_NUMBER_OF_TTI2EDCH_USERS_IN_SERVING_CELL(8),
    EVENT_PARAM_NUMBER_OF_TTI2EDCH_USERS_IN_NON_SERVING_CELL(8),
    EVENT_PARAM_UL_TN_IUB_USAGE(16),
    EVENT_PARAM_DL_TN_IUB_USAGE(16),
    EVENT_PARAM_EVENT_NR_OF_CS_SPEECH_USERS(8),
    EVENT_PARAM_SOURCE_CONNECTION_PROPERTIES(16),
    EVENT_PARAM_TARGET_CONNECTION_PROPERTIES(16),
    EVENT_PARAM_WANTED_CONF(8),
    EVENT_PARAM_WANTED_CONNECTION_PROPERTIES(16),
    EVENT_PARAM_PROCEDURE_INDICATOR(6),
    EVENT_PARAM_EVALUATION_CASE(6),
    EVENT_PARAM_MESSAGE_ID(17),
    EVENT_PARAM_MESSAGE_SERIAL_NO(17),
    EVENT_PARAM_TRIGGER(3),
    EVENT_PARAM_CN_ID(2),
    EVENT_PARAM_ORIGINATING_STATE(4),
    EVENT_PARAM_RRC_ESTABLISHMENT_CAUSE(6),
    EVENT_PARAM_CELLO_AAL2NCI_REJECT_REASON(5);
    private final int bits;
    private final Rules rule;

    /**
     * constructor
     */
    private Parameters(int bits) {
        this.bits = bits;
        rule = Rules.INTEGER;
    }

    /**
     * constructor
     * 
     * @param bits length
     * @param rule - interpretation rule
     */
    private Parameters(int bits, Rules rule) {
        this.bits = bits;
        this.rule = rule;
    }

    /**
     * <p>
     * interpretation rules
     * </p>
     * 
     * @author Cinkel_A
     * @since 1.0.0
     */
    public enum Rules {
        INTEGER, STRING, LONG;
    }

    /**
     * @return
     */
    public int getBitsLen() {
        return bits;
    }



    /**
     * @return Returns the rule.
     */
    public Rules getRule() {
        return rule;
    }

    /**
     *
     * @return
     */
    public boolean firstBitIsError() {
        return rule!=Rules.STRING;
    }
    
}
