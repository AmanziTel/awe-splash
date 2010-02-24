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

import static org.amanzi.neo.core.enums.gpeh.Parameters.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * GPEH events
 * </p>
 * @author Cinkel_A
 * @since 1.0.0
 */
public enum Events {
    INTERNAL_IMSI(384,EventType.RNC,EVENT_PARAM_IMSI),
    INTERNAL_RADIO_QUALITY_MEASUREMENTS_UEH(385,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_MEASURED_ENTITY,EVENT_PARAM_MEASURED_VALUE),
    INTERNAL_RADIO_QUALITY_MEASUREMENTS_RNH(386,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_MEASURED_ENTITY,EVENT_PARAM_MEASURED_VALUE),
    INTERNAL_CHANNEL_SWITCHING(387,EventType.RNC,EVENT_PARAM_IMSI,EVENT_PARAM_SOURCE_CONF,EVENT_PARAM_TARGET_CONF,EVENT_PARAM_CHANNEL_SWITCH_REASON,EVENT_PARAM_CHANNEL_SWITCH_RESULT,EVENT_PARAM_FAILURE_REASON,EVENT_PARAM_RLC_BUFFER_VOLUME,EVENT_PARAM_URA_ID,EVENT_PARAM_CELL_UPDATE_CAUSE_VALUE),
    INTERNAL_UL_OUTER_LOOP_POWER_CONTROL(388,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_UL_SIR_TARGET),
    INTERNAL_DL_POWER_MONITOR_UPDATE(389,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_ESTIMATED_POWER),
    INTERNAL_CELL_LOAD_MONITOR_UPDATE(390,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_TOTAL_ASE_UL,EVENT_PARAM_TOTAL_ASE_DL,EVENT_PARAM_TOTAL_DL_CHAN_CODE_TREE_CONSUMPTION,EVENT_PARAM_NR_OF_CONNECTIONS_SF8,EVENT_PARAM_NR_OF_CONNECTIONS_SF16,EVENT_PARAM_NR_OF_CONNECTIONS_SF32,EVENT_PARAM_NO_OF_CM_USERS,EVENT_PARAM_NUMBER_OF_USERS_ASSIGNED_TO_PHYS_HS_CHAN,EVENT_PARAM_NR_OF_CONNECTIONS_SF4_UL,EVENT_PARAM_NR_OF_CONNECTIONS_SF8_UL,EVENT_PARAM_NR_OF_CONNECTIONS_SF16_UL,EVENT_PARAM_NUMBER_OF_EDCH_USERS_IN_SERVING_CELL,EVENT_PARAM_NUMBER_OF_EDCH_USERS_IN_NON_SERVING_CELL),
    INTERNAL_ADMISSION_CONTROL_RESPONSE(391,EventType.RNC,EVENT_PARAM_RESULT,EVENT_PARAM_FAILURE_REASON,EVENT_PARAM_VALUE_AT_FAILURE,EVENT_PARAM_REQUEST_TYPE,EVENT_PARAM_TRAFFIC_CLASS,EVENT_PARAM_LOCAL_CELL_GROUP_ID,EVENT_PARAM_TRAFFIC_CLASS_UL),
    INTERNAL_CONGESTION_CONTROL_CHANNEL_SWITCH_AND_TERMINATE_RC(392,EventType.RNC,EVENT_PARAM_NO_OF_BE_USERS_TO_BE_SWITCHED,EVENT_PARAM_NO_OF_BE_USERS_TO_BE_SWITCHED_IUR,EVENT_PARAM_NO_OF_SPEECH_USERS_TO_BE_TERMINATED,EVENT_PARAM_NO_OF_SPEECH_USERS_TO_BE_TERMINATED_IUR,EVENT_PARAM_NR_OF_GUAR_DATA_USERS_TO_BE_TERMINATED,EVENT_PARAM_NR_OF_GUAR_DATA_USERS_TO_BE_TERMINATED_IUR,EVENT_PARAM_ASE_REDUCTION,EVENT_PARAM_NO_OF_HS_USERS_TO_BE_TERMINATED_IUR,EVENT_PARAM_NO_OF_PS_STREAMING_USERS_TO_BE_TERMINATED,EVENT_PARAM_NO_OF_PS_STREAMING_USERS_TO_BE_TERMINATED_IUR,EVENT_PARAM_NO_OF_HS_USERS_TO_BE_SWITCHED,EVENT_PARAM_NO_OF_EUL_USERS_TO_BE_SWITCHED),
    INTERNAL_START_CONGESTION(393,EventType.RNC,EVENT_PARAM_PREVIOUS_STATE,EVENT_PARAM_CURRENT_STATE,EVENT_PARAM_DL_POWER,EVENT_PARAM_UL_INTERFERENCE),
    INTERNAL_STOP_CONGESTION(394,EventType.RNC,EVENT_PARAM_PREVIOUS_STATE,EVENT_PARAM_CURRENT_STATE,EVENT_PARAM_DL_POWER,EVENT_PARAM_UL_INTERFERENCE),
    INTERNAL_DOWNLINK_CHANNELIZATION_CODE_ALLOCATION(395,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_CHANNELIZATION_CODE_NORM_MODE,EVENT_PARAM_CHANNELIZATION_CODE_COMP_MODE,EVENT_PARAM_SPREADING_FACTOR_NORM_MODE,EVENT_PARAM_SPREADING_FACTOR_COMP_MODE,EVENT_PARAM_CHANGED_NODE_SPREADING_FACTOR,EVENT_PARAM_CHANGED_NODE_CODE_NUMBER,EVENT_PARAM_CODE_TREE_LOAD,EVENT_PARAM_STATE_CHANGE_NODE),
    INTERNAL_IRAT_HO_CC_REPORT_RECEPTION(397,EventType.RNC,EVENT_PARAM_ACTION,EVENT_PARAM_NO_OF_CELLS_IN_REPORT,EVENT_PARAM_MEAS_REPORTS_IN_BUFFER_3A,EVENT_PARAM_IRAT_CELL_ID_1,EVENT_PARAM_IRAT_CELL_ID_2,EVENT_PARAM_IRAT_CELL_ID_3,EVENT_PARAM_IRAT_CELL_ID_4),
    INTERNAL_IRAT_HO_CC_EVALUATION(398,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_ACTION,EVENT_PARAM_SCRAMBLING_CODE_CELL_1,EVENT_PARAM_SCRAMBLING_CODE_CELL_2,EVENT_PARAM_SCRAMBLING_CODE_CELL_3,EVENT_PARAM_SCRAMBLING_CODE_CELL_4,EVENT_PARAM_CPICH_EC_NO_CELL_1,EVENT_PARAM_CPICH_EC_NO_CELL_2,EVENT_PARAM_CPICH_EC_NO_CELL_3,EVENT_PARAM_CPICH_EC_NO_CELL_4,EVENT_PARAM_RSCP_CELL_1,EVENT_PARAM_RSCP_CELL_2,EVENT_PARAM_RSCP_CELL_3,EVENT_PARAM_RSCP_CELL_4),
    INTERNAL_IRAT_HO_CC_EXECUTION(399,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_GSM_C_I,EVENT_PARAM_GSM_LAC,EVENT_PARAM_GSM_PLMN_ID,EVENT_PARAM_GSM_RSSI,EVENT_PARAM_SOURCE_CONF),
    INTERNAL_IRAT_CELL_CHANGE_DISCARDED_DATA(400,EventType.RNC,EVENT_PARAM_RLC_BUFFER_VOLUME),
    INTERNAL_CMODE_ACTIVATE(401,EventType.RNC,EVENT_PARAM_NO_OF_CELLS_IN_ACTIVE_SET,EVENT_PARAM_ACT_REASON,EVENT_PARAM_CM_TYPE),
    INTERNAL_CMODE_DEACTIVATE(402,EventType.RNC,EVENT_PARAM_NO_OF_CELLS_IN_ACTIVE_SET,EVENT_PARAM_DEACT_REASON,EVENT_PARAM_CM_TYPE),
    INTERNAL_RRC_ERROR(403,EventType.RNC,EVENT_PARAM_ERROR_TYPE,EVENT_PARAM_PROTOCOL_ERROR_CAUSE,EVENT_PARAM_RRC_TRANSACTION_IDENTIFIER,EVENT_PARAM_PROCEDURE_CODE,EVENT_PARAM_RECEIVED_MESSAGE_TYPE,EVENT_PARAM_ERROR_LOCATION,EVENT_PARAM_ASN1_AVAILABILITY,EVENT_PARAM_MESSAGE_LENGTH,EVENT_PARAM_MESSAGE_CONTENTS),
    INTERNAL_NBAP_ERROR(404,EventType.RNC,EVENT_PARAM_ERROR_TYPE,EVENT_PARAM_MESSAGE_DISCRIMINATOR,EVENT_PARAM_PROCEDURE_CODE,EVENT_PARAM_IE_NAME,EVENT_PARAM_ERROR_LOCATION,EVENT_PARAM_ASN1_AVAILABILITY,EVENT_PARAM_MESSAGE_LENGTH,EVENT_PARAM_MESSAGE_CONTENTS),
    INTERNAL_RANAP_ERROR(405,EventType.RNC,EVENT_PARAM_ERROR_TYPE,EVENT_PARAM_CN_DOMAIN_INDICATOR,EVENT_PARAM_PROCEDURE_CODE,EVENT_PARAM_IE_NAME,EVENT_PARAM_ERROR_LOCATION,EVENT_PARAM_ASN1_AVAILABILITY,EVENT_PARAM_MESSAGE_LENGTH,EVENT_PARAM_MESSAGE_CONTENTS),
    INTERNAL_SOFT_HANDOVER_REPORT_RECEPTION(406,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_ACTION,EVENT_PARAM_MEAS_REPORTS_IN_BUFFER_1A_1C,EVENT_PARAM_MEAS_REPORTS_IN_BUFFER_1B,EVENT_PARAM_MEAS_REPORTS_IN_BUFFER_1D,EVENT_PARAM_NO_OF_CELLS_IN_REPORT,EVENT_PARAM_SCRAMBLING_CODE_CELL_1,EVENT_PARAM_SCRAMBLING_CODE_CELL_2,EVENT_PARAM_SCRAMBLING_CODE_CELL_3,EVENT_PARAM_SCRAMBLING_CODE_CELL_4),
    INTERNAL_SOFT_HANDOVER_EVALUATION(407,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_ACTION,EVENT_PARAM_SCRAMBLING_CODE_CELL_1,EVENT_PARAM_SCRAMBLING_CODE_CELL_2,EVENT_PARAM_SCRAMBLING_CODE_CELL_3,EVENT_PARAM_SCRAMBLING_CODE_CELL_4,EVENT_PARAM_CPICH_EC_NO_CELL_1,EVENT_PARAM_CPICH_EC_NO_CELL_2,EVENT_PARAM_CPICH_EC_NO_CELL_3,EVENT_PARAM_CPICH_EC_NO_CELL_4,EVENT_PARAM_RSCP_CELL_1,EVENT_PARAM_RSCP_CELL_2,EVENT_PARAM_RSCP_CELL_3,EVENT_PARAM_RSCP_CELL_4),
    INTERNAL_SOFT_HANDOVER_EXECUTION(408,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_C_ID_EVALUATED,EVENT_PARAM_RNC_ID_EVALUATED,EVENT_PARAM_CPICH_EC_NO_EVAL_CELL,EVENT_PARAM_RSCP_EVAL_CELL,EVENT_PARAM_HANDOVER_TYPE,EVENT_PARAM_RESULT,EVENT_PARAM_FAILURE_REASON),
    INTERNAL_MEASUREMENT_HANDLING_EVALUATION(410,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_ACTION),
    INTERNAL_RNSAP_ERROR(413,EventType.RNC,EVENT_PARAM_ERROR_TYPE,EVENT_PARAM_PROCEDURE_CODE,EVENT_PARAM_IE_NAME,EVENT_PARAM_ERROR_LOCATION,EVENT_PARAM_ASN1_AVAILABILITY,EVENT_PARAM_MESSAGE_LENGTH,EVENT_PARAM_MESSAGE_CONTENTS),
    INTERNAL_RC_SUPERVISION(414,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_ACTION,EVENT_PARAM_RLS_ID_1,EVENT_PARAM_RLS_ID_2,EVENT_PARAM_RLS_ID_3,EVENT_PARAM_RLS_ID_4,EVENT_PARAM_RLS_TAG_1,EVENT_PARAM_RLS_TAG_2,EVENT_PARAM_RLS_TAG_3,EVENT_PARAM_RLS_TAG_4),
    INTERNAL_RAB_ESTABLISHMENT(415,EventType.RNC,EVENT_PARAM_SOURCE_CONF,EVENT_PARAM_TARGET_CONF,EVENT_PARAM_RESULT,EVENT_PARAM_FAILURE_REASON,EVENT_PARAM_HS_DSCH_PHYSICAL_LAYER_CATEGORY,EVENT_PARAM_E_DCH_PHYSICAL_LAYER_CATEGORY,EVENT_PARAM_E_DCH_AND_OR_HS_DSCH_CELL_SELECTION_RESULT,EVENT_PARAM_E_DCH_AND_OR_HS_DSCH_CELL_ID,EVENT_PARAM_TRAFFIC_CLASS),
    INTERNAL_RAB_RELEASE(416,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_SOURCE_CONF,EVENT_PARAM_TARGET_CONF,EVENT_PARAM_RESULT,EVENT_PARAM_FAILURE_REASON,EVENT_PARAM_E_DCH_AND_OR_HS_DSCH_CELL_ID),
    INTERNAL_UE_MOVE(417,EventType.RNC),
    INTERNAL_UPLINK_SCRAMBLING_CODE_ALLOCATION(418,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_UL_SCRAMBLING_CODE),
    INTERNAL_MEASUREMENT_HANDLING_EXECUTION(419,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_ACTION),
    INTERNAL_IFHO_REPORT_RECEPTION(420,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_ACTION,EVENT_PARAM_NO_OF_CELLS_IN_REPORT,EVENT_PARAM_SCRAMBLING_CODE_REP_CELL_1,EVENT_PARAM_UARFCN_DL_REP_CELL_1,EVENT_PARAM_UARFCN_UL_REP_CELL_1,EVENT_PARAM_SCRAMBLING_CODE_REP_CELL_2,EVENT_PARAM_UARFCN_DL_REP_CELL_2,EVENT_PARAM_UARFCN_UL_REP_CELL_2,EVENT_PARAM_SCRAMBLING_CODE_REP_CELL_3,EVENT_PARAM_UARFCN_DL_REP_CELL_3,EVENT_PARAM_UARFCN_UL_REP_CELL_3,EVENT_PARAM_SCRAMBLING_CODE_REP_CELL_4,EVENT_PARAM_UARFCN_DL_REP_CELL_4,EVENT_PARAM_UARFCN_UL_REP_CELL_4,EVENT_PARAM_MEAS_REPORTS_IN_BUFFER_2B),
    INTERNAL_IFHO_EVALUATION(421,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_ACTION,EVENT_PARAM_SCRAMBLING_CODE_CELL_1,EVENT_PARAM_CPICH_EC_NO_CELL_1,EVENT_PARAM_RSCP_CELL_1,EVENT_PARAM_SCRAMBLING_CODE_CELL_2,EVENT_PARAM_CPICH_EC_NO_CELL_2,EVENT_PARAM_RSCP_CELL_2,EVENT_PARAM_SCRAMBLING_CODE_CELL_3,EVENT_PARAM_CPICH_EC_NO_CELL_3,EVENT_PARAM_RSCP_CELL_3,EVENT_PARAM_SCRAMBLING_CODE_CELL_4,EVENT_PARAM_CPICH_EC_NO_CELL_4,EVENT_PARAM_RSCP_CELL_4),
    INTERNAL_IFHO_EXECUTION(422,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_C_ID_NU_FREQ,EVENT_PARAM_RNC_ID_NU_FREQ,EVENT_PARAM_CPICH_EC_NU_FREQ_CELL,EVENT_PARAM_RSCP_NU_FREQ_CELL,EVENT_PARAM_SOURCE_CONF),
    INTERNAL_IFHO_EXECUTION_ACTIVE(423,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_C_ID_NU_FREQ,EVENT_PARAM_RNC_ID_NU_FREQ,EVENT_PARAM_CPICH_EC_NU_FREQ_CELL,EVENT_PARAM_RSCP_NU_FREQ_CELL,EVENT_PARAM_SOURCE_CONF,EVENT_PARAM_RESULT,EVENT_PARAM_FAILURE_REASON),
    INTERNAL_RBS_HW_MONITOR_UPDATE(426,EventType.RNC,EVENT_PARAM_LOCAL_CELL_GROUP_ID,EVENT_PARAM_RBS_ID,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_CAPACITY_CREDITS_UL,EVENT_PARAM_CAPACITY_CREDITS_DL,EVENT_PARAM_CONSUMED_CREDITS_UL,EVENT_PARAM_CONSUMED_CREDITS_DL),
    //TODO overwrite parameter EVENT_PARAM_UE_CONTEXT!!
    INTERNAL_SOHO_DS_MISSING_NEIGHBOUR(427,EventType.RNC,EVENT_PARAM_UE_CONTEXT,EVENT_PARAM_ACTION,EVENT_PARAM_SCRAMBLING_CODE_CELL_1,EVENT_PARAM_SCRAMBLING_CODE_CELL_2,EVENT_PARAM_SCRAMBLING_CODE_CELL_3,EVENT_PARAM_SCRAMBLING_CODE_CELL_4,EVENT_PARAM_CPICH_EC_NO_CELL_1,EVENT_PARAM_CPICH_EC_NO_CELL_2,EVENT_PARAM_CPICH_EC_NO_CELL_3,EVENT_PARAM_CPICH_EC_NO_CELL_4,EVENT_PARAM_RSCP_CELL_1,EVENT_PARAM_RSCP_CELL_2,EVENT_PARAM_RSCP_CELL_3,EVENT_PARAM_RSCP_CELL_4,EVENT_PARAM_SCRAMBLING_CODE_TRIGGER_CELL,EVENT_PARAM_CPICH_EC_NO_TRIGGER_CELL,EVENT_PARAM_RSCP_TRIGGER_CELL,EVENT_PARAM_SCRAMBLING_CODE_ADDITIONAL_CELL1,EVENT_PARAM_CPICH_EC_NO_ADDITIONAL_CELL1,EVENT_PARAM_RSCP_ADDITIONAL_CELL1,EVENT_PARAM_SCRAMBLING_CODE_ADDITIONAL_CELL2,EVENT_PARAM_CPICH_EC_NO_ADDITIONAL_CELL2,EVENT_PARAM_RSCP_ADDITIONAL_CELL2),
    INTERNAL_SOHO_DS_UNMONITORED_NEIGHBOUR(428,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_CPICH_EC_NO_CELL_1,EVENT_PARAM_SCRAMBLING_CODE_CELL_1,EVENT_PARAM_SCRAMBLING_CODE_CELL_2,EVENT_PARAM_SCRAMBLING_CODE_CELL_3,EVENT_PARAM_SCRAMBLING_CODE_CELL_4,EVENT_PARAM_SCRAMBLING_CODE_TRIGGER_CELL,EVENT_PARAM_CPICH_EC_NO_TRIGGER_CELL,EVENT_PARAM_RSCP_TRIGGER_CELL,EVENT_PARAM_C_ID_TRIGGER_CELL,EVENT_PARAM_RNC_ID_TRIGGER_CELL),
    INTERNAL_UE_POSITIONING_QOS(429,EventType.RNC,EVENT_PARAM_UE_POS_METHOD,EVENT_PARAM_NR_GPS_SATELLITES,EVENT_PARAM_MEASUREMENT_TIME,EVENT_PARAM_REQUIRED_HORIZONTAL_ACCURACY,EVENT_PARAM_ACHIEVED_HORIZONTAL_ACCURACY,EVENT_PARAM_REQUIRED_VERTICAL_ACCURACY,EVENT_PARAM_ACHIEVED_VERTICAL_ACCURACY,EVENT_PARAM_UE_REFERENCE_POSITION,EVENT_PARAM_IMSI,EVENT_PARAM_LATITUDE_SIGN,EVENT_PARAM_LATITUDE,EVENT_PARAM_LONGITUDE_SIGN,EVENT_PARAM_LONGITUDE,EVENT_PARAM_ALTITUDE_DIRECTION,EVENT_PARAM_ALTITUDE_DIRECTION,EVENT_PARAM_UNCERTAINTY_SEMI_MAJOR,EVENT_PARAM_UNCERTAINTY_SEMI_MINOR,EVENT_PARAM_ORIENTATION_MAJOR_AXIS,EVENT_PARAM_CONFIDENCE,EVENT_PARAM_UEPOS_INITIAL_CONF,EVENT_PARAM_UEPOS_FINAL_CONF),
    INTERNAL_UE_POSITIONING_UNSUCC(430,EventType.RNC,EVENT_PARAM_UE_CONTEXT,EVENT_PARAM_UE_POS_UNSUCC_CAUSE,EVENT_PARAM_UEPOS_INITIAL_CONF,EVENT_PARAM_UEPOS_FINAL_CONF),
    INTERNAL_SYSTEM_BLOCK(431,EventType.RNC,EVENT_PARAM_REQUESTED_SERVICE_CLASS,EVENT_PARAM_REQUESTED_SETUP_CLASS,EVENT_PARAM_REQUESTED_ORIGIN,EVENT_PARAM_REQUESTED_SF_DL,EVENT_PARAM_REQUESTED_SF_UL,EVENT_PARAM_REQUESTED_COMPRESSED_MODE,EVENT_PARAM_CURRENT_SF_DL,EVENT_PARAM_CURRENT_SF_UL,EVENT_PARAM_CURRENT_RAB_STATE,EVENT_PARAM_BLOCKING_PROCEDURE,EVENT_PARAM_BLOCK_REASON,EVENT_PARAM_BLOCK_TYPE,EVENT_PARAM_BLOCK_ADMISSION_POLICY_LEVEL,EVENT_PARAM_TRAFFIC_CLASS_UL,EVENT_PARAM_SOURCE_CONF,EVENT_PARAM_TARGET_CONF),
    INTERNAL_SUCCESSFUL_HSDSCH_CELL_CHANGE(432,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_C_ID_SOURCE,EVENT_PARAM_CPICH_EC_NO_SOURCE_CELL,EVENT_PARAM_CPICH_RSCP_SOURCE_CELL,EVENT_PARAM_C_ID_TARGET,EVENT_PARAM_CPICH_EC_NO_TARGET_CELL,EVENT_PARAM_CPICH_RSCP_TARGET_CELL,EVENT_PARAM_SOURCE_CONF),
    INTERNAL_FAILED_HSDSCH_CELL_CHANGE(433,EventType.RNC,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_C_ID_SOURCE,EVENT_PARAM_CPICH_EC_NO_SOURCE_CELL,EVENT_PARAM_CPICH_RSCP_SOURCE_CELL,EVENT_PARAM_C_ID_TARGET,EVENT_PARAM_CPICH_EC_NO_TARGET_CELL,EVENT_PARAM_CPICH_RSCP_TARGET_CELL,EVENT_PARAM_FAILURE_REASON,EVENT_PARAM_SOURCE_CONF),
    INTERNAL_SUCCESSFUL_HSDSCH_CELL_SELECTION_OLD_ACTIVE_SET(434,EventType.RNC,EVENT_PARAM_C_ID_TARGET,EVENT_PARAM_SELECTED_CELL,EVENT_PARAM_PATHLOSS,EVENT_PARAM_SOURCE_CONF,EVENT_PARAM_TARGET_CAP),
    INTERNAL_SUCCESSFUL_HSDSCH_CELL_SELECTION_NEW_ACTIVE_SET(435,EventType.RNC,EVENT_PARAM_C_ID_TARGET,EVENT_PARAM_UARFCN_SOURCE,EVENT_PARAM_UARFCN_TARGET,EVENT_PARAM_PATHLOSS,EVENT_PARAM_SOURCE_CONF,EVENT_PARAM_TARGET_CAP),
    INTERNAL_HSDSCH_CELL_SELECTION_NO_CELL_SELECTED(436,EventType.RNC,EVENT_PARAM_C_ID_TARGET,EVENT_PARAM_UARFCN_SOURCE,EVENT_PARAM_UARFCN_TARGET,EVENT_PARAM_PATHLOSS,EVENT_PARAM_FAILURE_REASON,EVENT_PARAM_UL_SYNC_STATUS_RLS1,EVENT_PARAM_SOURCE_CONF),
    INTERNAL_TWO_NON_USED_FREQ_EXCEEDED(437,EventType.RNC,EVENT_PARAM_CPICH_EC_NO_CELL_1,EVENT_PARAM_CPICH_EC_NO_CELL_2,EVENT_PARAM_CPICH_EC_NO_CELL_3,EVENT_PARAM_CPICH_EC_NO_CELL_4,EVENT_PARAM_RSCP_CELL_1,EVENT_PARAM_RSCP_CELL_2,EVENT_PARAM_RSCP_CELL_3,EVENT_PARAM_RSCP_CELL_4,EVENT_PARAM_UARFCN_DL_NON_USED_FREQ_1,EVENT_PARAM_UARFCN_DL_NON_USED_FREQ_2,EVENT_PARAM_UARFCN_DL_NON_USED_FREQ_3),
    INTERNAL_SYSTEM_RELEASE(438,EventType.RNC,EVENT_PARAM_IMSI,EVENT_PARAM_SOURCE_CONF,EVENT_PARAM_TARGET_CONF,EVENT_PARAM_TRIGGER_POINT,EVENT_PARAM_RAN_DISCONNECTION_CODE,EVENT_PARAM_RAN_DISCONNECTION_SUBCODE,EVENT_PARAM_RAN_ID_PARAMETER,EVENT_PARAM_CPM,EVENT_PARAM_SCRAMBLING_CODE_CELL_1,EVENT_PARAM_SCRAMBLING_CODE_CELL_2,EVENT_PARAM_SCRAMBLING_CODE_CELL_3,EVENT_PARAM_SCRAMBLING_CODE_CELL_4,EVENT_PARAM_CPICH_EC_NO_CELL_1,EVENT_PARAM_CPICH_EC_NO_CELL_2,EVENT_PARAM_CPICH_EC_NO_CELL_3,EVENT_PARAM_CPICH_EC_NO_CELL_4,EVENT_PARAM_SCRAMBLING_CODE_ADDED_CELL,EVENT_PARAM_CPICH_EC_NO_ADDED_CELL,EVENT_PARAM_UL_INT_CELL1,EVENT_PARAM_UL_INT_CELL2,EVENT_PARAM_UL_INT_CELL3,EVENT_PARAM_UL_INT_CELL4,EVENT_PARAM_CN_ID),
    INTERNAL_CNHHO_EXECUTION_ACTIVE(439,EventType.RNC,EVENT_PARAM_C_ID_EVALUATED,EVENT_PARAM_CPICH_EC_NO_EVAL_CELL,EVENT_PARAM_RSCP_EVAL_CELL,EVENT_PARAM_CNHHO_TYPE,EVENT_PARAM_SOURCE_CONF,EVENT_PARAM_IUR_STATUS,EVENT_PARAM_RESULT,EVENT_PARAM_FAILURE_REASON),
    INTERNAL_PS_RELEASE_DUE_TO_CNHHO(440,EventType.RNC,EVENT_PARAM_C_ID_EVALUATED,EVENT_PARAM_CPICH_EC_NO_EVAL_CELL,EVENT_PARAM_RSCP_EVAL_CELL,EVENT_PARAM_SOURCE_CONF,EVENT_PARAM_IUR_STATUS,EVENT_PARAM_RESULT,EVENT_PARAM_FAILURE_REASON),
    INTERNAL_PACKET_DEDICATED_THROUGHPUT(441,EventType.RNC,EVENT_PARAM_RAB_DATA_RATE_UL,EVENT_PARAM_RAB_DATA_RATE_DL,EVENT_PARAM_UL_THROUGHPUT,EVENT_PARAM_DL_THROUGHPUT),
    INTERNAL_SUCCESSFUL_TRANSITION_TO_DCH(442,EventType.RNC,EVENT_PARAM_CPICH_EC_NO_CELL_1,EVENT_PARAM_RSCP_CELL_1,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_C_ID_SERV_HSDSCH_CELL,EVENT_PARAM_CRNC_ID_SERV_HSDSCH_CELL,EVENT_PARAM_EC_NO_SERV_HSDSCH_CELL,EVENT_PARAM_RSCP_SERV_HSDSCH_CELL,EVENT_PARAM_SOURCE_CONF,EVENT_PARAM_TARGET_CONF),
    NTERNAL_FAILED_TRANSITION_TO_DCH(443,EventType.RNC,EVENT_PARAM_CPICH_EC_NO_CELL_1,EVENT_PARAM_RSCP_CELL_1,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_C_ID_SERV_HSDSCH_CELL,EVENT_PARAM_CRNC_ID_SERV_HSDSCH_CELL,EVENT_PARAM_EC_NO_SERV_HSDSCH_CELL,EVENT_PARAM_RSCP_SERV_HSDSCH_CELL,EVENT_PARAM_SOURCE_CONF,EVENT_PARAM_TARGET_CONF,EVENT_PARAM_FAILURE_REASON),
    INTERNAL_RECORDING_FAULT(444,EventType.RNC,EVENT_PARAM_RNC_MP_ID,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_AFFECTED_SCANNER_TYPE),
    INTERNAL_RECORDING_RECOVERED(445,EventType.RNC,EVENT_PARAM_RNC_MP_ID,EVENT_PARAM_EVENT_TRIGGER,EVENT_PARAM_AFFECTED_SCANNER_TYPE),
    RRC_ACTIVE_SET_UPDATE(0,EventType.RRC),
    RRC_ACTIVE_SET_UPDATE_COMPLETE(1,EventType.RRC),
    RRC_ACTIVE_SET_UPDATE_FAILURE(2,EventType.RRC),
    RRC_CELL_UPDATE(3,EventType.RRC),
    RRC_CELL_UPDATE_CONFIRM(4,EventType.RRC),
    RRC_DOWNLINK_DIRECT_TRANSFER(5,EventType.RRC),
    RRC_MEASUREMENT_CONTROL(6,EventType.RRC),
    RRC_MEASUREMENT_CONTROL_FAILURE(7,EventType.RRC),
    RRC_MEASUREMENT_REPORT(8,EventType.RRC),
    RRC_PAGING_TYPE_2(9,EventType.RRC),
    RRC_RADIO_BEARER_RECONFIGURATION(10,EventType.RRC),
    RRC_RADIO_BEARER_RECONFIGURATION_COMPLETE(11,EventType.RRC),
    RRC_RADIO_BEARER_RECONFIGURATION_FAILURE(12,EventType.RRC),
    RRC_RADIO_BEARER_RELEASE(13,EventType.RRC),
    RRC_RADIO_BEARER_RELEASE_COMPLETE(14,EventType.RRC),
    RRC_RADIO_BEARER_RELEASE_FAILURE(15,EventType.RRC),
    RRC_RADIO_BEARER_SETUP(16,EventType.RRC),
    RRC_RADIO_BEARER_SETUP_COMPLETE(17,EventType.RRC),
    RRC_RADIO_BEARER_SETUP_FAILURE(18,EventType.RRC),
    RRC_RRC_CONNECTION_RELEASE(19,EventType.RRC),
    RRC_RRC_CONNECTION_RELEASE_COMPLETE(20,EventType.RRC),
    RRC_RRC_STATUS(21,EventType.RRC),
    RRC_SECURITY_MODE_COMMAND(22,EventType.RRC),
    RRC_SECURITY_MODE_COMPLETE(23,EventType.RRC),
    RRC_SECURITY_MODE_FAILURE(24,EventType.RRC),
    RRC_SIGNALLING_CONNECTION_RELEASE(25,EventType.RRC),
    RRC_SIGNALLING_CONNECTION_RELEASE_INDICATION(26,EventType.RRC),
    RRC_UE_CAPABILITY_INFORMATION(27,EventType.RRC),
    RRC_UE_CAPABILITY_INFORMATION_CONFIRM(28,EventType.RRC),
    RRC_UPLINK_DIRECT_TRANSFER(29,EventType.RRC),
    RRC_UTRAN_MOBILITY_INFORMATION_CONFIRM(30,EventType.RRC),
    RRC_INITIAL_DIRECT_TRANSFER(31,EventType.RRC),
    RRC_RRC_CONNECTION_REJECT(33,EventType.RRC),
    RRC_RRC_CONNECTION_REQUEST(34,EventType.RRC),
    RRC_RRC_CONNECTION_SETUP(35,EventType.RRC),
    RRC_RRC_CONNECTION_SETUP_COMPLETE(36,EventType.RRC),
    RRC_SYSTEM_INFORMATION_CHANGE_INDICATION(37,EventType.RRC),
    RRC_HANDOVER_FROM_UTRAN_COMMAND(38,EventType.RRC),
    RRC_HANDOVER_FROM_UTRAN_FAILURE(39,EventType.RRC),
    RRC_PHYSICAL_CHANNEL_RECONFIGURATION(40,EventType.RRC),
    RRC_PHYSICAL_CHANNEL_RECONFIGURATION_COMPLITE(41,EventType.RRC),
    RRC_PHYSICAL_CHANNEL_RECONFIGURATION_FAILURE(42,EventType.RRC),
    RRC_UTRAN_MOBILITY_INFORMATION(43,EventType.RRC),
    RRC_UTRAN_MOBILITY_INFORMATION_FAILURE(44,EventType.RRC),
    RRC_CELL_CHANGE_ORDER_FROM_UTRAN(45,EventType.RRC),
    RRC_CELL_CHANGE_ORDER_FROM_UTRAN_FAILURE(46,EventType.RRC),
    RRC_UE_CAPABILITY_ENQUIRY(47,EventType.RRC),
    RRC_URA_UPDATE(48,EventType.RRC),
    RRC_URA_UPDATE_CONFIRM(49,EventType.RRC),
    RRC_TRANSPORT_CHANNEL_RECONFIGURATION(50,EventType.RRC),
    RRC_TRANSPORT_CHANNEL_RECONFIGURATION_COMPLETE(51,EventType.RRC),
    
    NBAP_RADIO_LINK_SETUP_REQUEST(128,EventType.NBAP),
    NBAP_RADIO_LINK_SETUP_RESPONSE(129,EventType.NBAP),
    NBAP_RADIO_LINK_SETUP_FAILURE(130,EventType.NBAP),
    NBAP_RADIO_LINK_ADDITION_REQUEST(131,EventType.NBAP),
    NBAP_RADIO_LINK_ADDITION_RESPONSE(132,EventType.NBAP),
    NBAP_RADIO_LINK_ADDITION_FAILURE(133,EventType.NBAP),
    NBAP_RADIO_LINK_RECONFIGURATION_PREPARE(134,EventType.NBAP),
    NBAP_RADIO_LINK_RECONFIGURATION_READY(135,EventType.NBAP),
    NBAP_RADIO_LINK_RECONFIGURATION_FAILURE(136,EventType.NBAP),
    NBAP_RADIO_LINK_RECONFIGURATION_COMMIT(137,EventType.NBAP),
    NBAP_RADIO_LINK_RECONFIGURATION_CANCEL(138,EventType.NBAP),
    NBAP_RADIO_LINK_DELETION_REQUEST(139,EventType.NBAP),
    NBAP_RADIO_LINK_DELETION_RESPONSE(140,EventType.NBAP),
    NBAP_DL_POWER_CONTROL_REQUEST(141,EventType.NBAP),
    NBAP_DEDICATED_MEASUREMENT_INITIATION_REQUEST(142,EventType.NBAP),
    NBAP_DEDICATED_MEASUREMENT_INITIATION_RESPONSE(143,EventType.NBAP),
    NBAP_DEDICATED_MEASUREMENT_INITIATION_FAILURE(144,EventType.NBAP),
    NBAP_DEDICATED_MEASUREMENT_REPORT(145,EventType.NBAP),
    NBAP_DEDICATED_MEASUREMENT_TERMINATION_REQUEST(146,EventType.NBAP),
    NBAP_DEDICATED_MEASUREMENT_FAILURE_INDICATION(147,EventType.NBAP),
    NBAP_RADIO_LINK_FAILURE_INDICATION(148,EventType.NBAP),
    NBAP_RADIO_LINK_RESTORE_INDICATION(149,EventType.NBAP),
    NBAP_ERROR_INDICATION(150,EventType.NBAP),
    NBAP_COMMON_TRANSPORT_CHANNEL_SETUP_REQUEST(151,EventType.NBAP),
    NBAP_COMMON_TRANSPORT_CHANNEL_SETUP_RESPONSE(152,EventType.NBAP),
    NBAP_COMMON_TRANSPORT_CHANNEL_SETUP_FAILURE(153,EventType.NBAP),
    NBAP_COMMON_TRANSPORT_CHANNEL_RECONFIGURATION_REQUEST(154,EventType.NBAP),
    NBAP_COMMON_TRANSPORT_CHANNEL_RECONFIGURATION_RESPONSE(155,EventType.NBAP),
    NBAP_COMMON_TRANSPORT_CHANNEL_RECONFIGURATION_FAILURE(156,EventType.NBAP),
    NBAP_COMMON_TRANSPORT_CHANNEL_DELETION_REQUEST(157,EventType.NBAP),
    NBAP_COMMON_TRANSPORT_CHANNEL_DELETION_RESPONSE(158,EventType.NBAP),
    NBAP_AUDIT_REQUIRED_INDICATION(159,EventType.NBAP),
    NBAP_AUDIT_REQUEST(160,EventType.NBAP),
    NBAP_AUDIT_RESPONSE(161,EventType.NBAP),
    NBAP_AUDIT_FAILURE(162,EventType.NBAP),
    NBAP_COMMON_MEASUREMENT_INITIATION_REQUEST(163,EventType.NBAP),
    NBAP_COMMON_MEASUREMENT_INITIATION_RESPONSE(164,EventType.NBAP),
    NBAP_COMMON_MEASUREMENT_INITIATION_FAILURE(165,EventType.NBAP),
    NBAP_COMMON_MEASUREMENT_REPORT(166,EventType.NBAP),
    NBAP_COMMON_MEASUREMENT_TERMINATION_REQUEST(167,EventType.NBAP),
    NBAP_COMMON_MEASUREMENT_FAILURE_INDICATION(168,EventType.NBAP),
    NBAP_CELL_SETUP_REQUEST(169,EventType.NBAP),
    NBAP_CELL_SETUP_RESPONSE(170,EventType.NBAP),
    NBAP_CELL_SETUP_FAILURE(171,EventType.NBAP),
    NBAP_CELL_RECONFIGURATION_REQUEST(172,EventType.NBAP),
    NBAP_CELL_RECONFIGURATION_RESPONSE(173,EventType.NBAP),
    NBAP_CELL_RECONFIGURATION_FAILURE(174,EventType.NBAP),
    NBAP_CELL_DELETION_REQUEST(175,EventType.NBAP),
    NBAP_CELL_DELETION_RESPONSE(176,EventType.NBAP),
    NBAP_RESOURCE_STATUS_INDICATION(177,EventType.NBAP),
    NBAP_SYSTEM_INFORMATION_UPDATE_REQUEST(178,EventType.NBAP),
    NBAP_SYSTEM_INFORMATION_UPDATE_RESPON(179,EventType.NBAP),
    NBAP_SYSTEM_INFORMATION_UPDATE_FAILURE(180,EventType.NBAP),
    NBAP_RESET_REQUEST(181,EventType.NBAP),
    NBAP_RESET_RESPONSE(182,EventType.NBAP),
    NBAP_COMPRESSED_MODE_COMMAND(183,EventType.NBAP),
    NBAP_RADIO_LINK_PARAMETER_UPDATE_INDICATION(184,EventType.NBAP),
    NBAP_PHYSICAL_SHARED_CHANNEL_RECONFIGURATION_REQUEST(185,EventType.NBAP),
    NBAP_PHYSICAL_SHARED_CHANNEL_RECONFIGURATION_RESPONSE(186,EventType.NBAP),
    NBAP_PHYSICAL_SHARED_CHANNEL_RECONFIGURATION_FAILURE(187,EventType.NBAP),
    
    RANAP_RAB_ASSIGNMENT_REQUEST(256,EventType.RANAP),
    RANAP_RAB_ASSIGNMENT_RESPONSE(257,EventType.RANAP),
    RANAP_IU_RELEASE_REQUEST(258,EventType.RANAP),
    RANAP_IU_RELEASE_COMMAND(259,EventType.RANAP),
    RANAP_IU_RELEASE_COMPLETE(260,EventType.RANAP),
    RANAP_SECURITY_MODE_COMMAND(261,EventType.RANAP),
    RANAP_SECURITY_MODE_COMPLETE(262,EventType.RANAP),
    RANAP_SECURITY_MODE_REJECT(263,EventType.RANAP),
    RANAP_LOCATION_REPORTING_CONTROL(264,EventType.RANAP),
    RANAP_DIRECT_TRANSFER(265,EventType.RANAP),
    RANAP_ERROR_INDICATION(266,EventType.RANAP),
    RANAP_PAGING(267,EventType.RANAP),
    RANAP_COMMON_ID(268,EventType.RANAP),
    RANAP_INITIAL_UE_MESSAGE(269,EventType.RANAP),
    RANAP_RESET(270,EventType.RANAP),
    RANAP_RESET_ACKNOWLEDGE(271,EventType.RANAP),
    RANAP_RESET_RESOURCE(272,EventType.RANAP),
    RANAP_RESET_RESOURCE_ACKNOWLEDGE(273,EventType.RANAP),
    RANAP_RELOCATION_REQUIRED(274,EventType.RANAP),
    RANAP_RELOCATION_REQUEST(275,EventType.RANAP),
    RANAP_RELOCATION_REQUEST_ACKNOWLEDGE(276,EventType.RANAP),
    RANAP_RELOCATION_COMMAND(277,EventType.RANAP),
    RANAP_RELOCATION_DETECT(278,EventType.RANAP),
    RANAP_RELOCATION_COMPLETE(279,EventType.RANAP),
    RANAP_RELOCATION_PREPARATION_FAILURE(280,EventType.RANAP),
    RANAP_RELOCATION_FAILURE(281,EventType.RANAP),
    RANAP_RELOCATION_CANCEL(282,EventType.RANAP),
    RANAP_RELOCATION_CANCEL_ACKNOWLEDGE(283,EventType.RANAP),
    RANAP_SRNS_CONTEXT_REQUEST(284,EventType.RANAP),
    RANAP_SRNS_CONTEXT_RESPONSE(285,EventType.RANAP),
    RANAP_SRNS_DATA_FORWARD_COMMAND(286,EventType.RANAP),
    RANAP_LOCATION_REPORT(287,EventType.RANAP),
    RANAP_RANAP_RELOCATION_INFORMATION(289,EventType.RANAP),
    RANAP_RAB_RELEASE_REQUEST(290,EventType.RANAP),
    
    RNSAP_COMMON_TRANSPORT_CHANNEL_RESOURCES_RELEASE_REQUEST(512,EventType.RNSAP),
    RNSAP_COMMON_TRANSPORT_CHANNEL_RESOURCES_REQUEST(513,EventType.RNSAP),
    RNSAP_COMMON_TRANSPORT_CHANNEL_RESOURCES_RESPONSE(514,EventType.RNSAP),
    RNSAP_COMMON_TRANSPORT_CHANNEL_RESOURCES_FAILURE(515,EventType.RNSAP),
    RNSAP_RADIO_LINK_SETUP_REQUEST(516,EventType.RNSAP),
    RNSAP_RADIO_LINK_SETUP_RESPONSE(517,EventType.RNSAP),
    RNSAP_RADIO_LINK_SETUP_FAILURE(518,EventType.RNSAP),
    RNSAP_RADIO_LINK_ADDITION_REQUEST(519,EventType.RNSAP),
    RNSAP_RADIO_LINK_ADDITION_RESPONSE(520,EventType.RNSAP),
    RNSAP_RADIO_LINK_ADDITION_FAILURE(521,EventType.RNSAP),
    RNSAP_RADIO_LINK_RECONFIGURATION_PREPARE(522,EventType.RNSAP),
    RNSAP_RADIO_LINK_RECONFIGURATION_READY(523,EventType.RNSAP),
    RNSAP_RADIO_LINK_RECONFIGURATION_FAILURE(524,EventType.RNSAP),
    RNSAP_RADIO_LINK_RECONFIGURATION_COMMIT(525,EventType.RNSAP),
    RNSAP_RADIO_LINK_RECONFIGURATION_CANCEL(526,EventType.RNSAP),
    RNSAP_RADIO_LINK_DELETION_REQUEST(527,EventType.RNSAP),
    RNSAP_RADIO_LINK_DELETION_RESPONSE(528,EventType.RNSAP),
    RNSAP_DL_POWER_CONTROL_REQUEST(529,EventType.RNSAP),
    RNSAP_DEDICATED_MEASUREMENT_INITIATION_REQUEST(530,EventType.RNSAP),
    RNSAP_DEDICATED_MEASUREMENT_INITIATION_RESPONSE(531,EventType.RNSAP),
    RNSAP_DEDICATED_MEASUREMENT_INITIATION_FAILURE(532,EventType.RNSAP),
    RNSAP_DEDICATED_MEASUREMENT_REPORT(533,EventType.RNSAP),
    RNSAP_DEDICATED_MEASUREMENT_TERMINATION_REQUEST(534,EventType.RNSAP),
    RNSAP_DEDICATED_MEASUREMENT_FAILURE_INDICATION(535,EventType.RNSAP),
    RNSAP_RADIO_LINK_FAILURE_INDICATION(536,EventType.RNSAP),
    RNSAP_RADIO_LINK_RESTORE_INDICATION(537,EventType.RNSAP),
    RNSAP_COMPRESSED_MODE_COMMAND(538,EventType.RNSAP),
    RNSAP_ERROR_INDICATION(539,EventType.RNSAP),
    RNSAP_UPLINK_SIGNALLING_TRANSFER_INDICATION(540,EventType.RNSAP),
    RNSAP_DOWNLINK_SIGNALLING_TRANSFER_REQUEST(541,EventType.RNSAP);

    // event id
    private final int id;
    // event type
    private final EventType eventType;
    private final ArrayList<Parameters> additionalParameters;

    /**
     * constructor
     */
    private Events(int id, EventType type, Parameters... additionalParameters) {
        this.id = id;
        this.eventType = type;
        this.additionalParameters = new ArrayList<Parameters>();
        if (additionalParameters != null) {
            for (Parameters parameters : additionalParameters) {
                this.additionalParameters.add(parameters);
            }
        }
    }

    /**
     * get all parameters of event
     * 
     * @return
     */
    public List<Parameters> getAllParameters() {
        List<Parameters> result = new LinkedList<Parameters>();
        result.add(EVENT_PARAM_UE_CONTEXT);
        result.add(EVENT_PARAM_RNC_MODULE_ID);
        result.add(EVENT_PARAM_C_ID_1);
        result.add(EVENT_PARAM_RNC_ID_1);
        result.add(EVENT_PARAM_C_ID_2);
        result.add(EVENT_PARAM_RNC_ID_2);
        result.add(EVENT_PARAM_C_ID_3);
        result.add(EVENT_PARAM_RNC_ID_3);
        result.add(EVENT_PARAM_C_ID_4);
        result.add(EVENT_PARAM_RNC_ID_4);
        if (eventType!=EventType.RNC){
            if (eventType==EventType.RRC){
                result.add(EVENT_PARAM_PDU_TYPE);
            }
            result.add(EVENT_PARAM_PROTOCOL_ID);
            result.add(EVENT_PARAM_MESSAGE_DIRECTION);
            result.add(EVENT_PARAM_MESSAGE_LENGTH);
//            result.add(EVENT_PARAM_MESSAGE_CONTENTS);
        }
        result.addAll(additionalParameters);
        return result;
    }

    /**
     * find enum by id
     * @param id - event id
     * @return event or null
     */
    public static Events findById(int id) {
        for (Events event:Events.values()){
            if (event.id==id){
                return event;
            }
        }
        return null;
    }
}