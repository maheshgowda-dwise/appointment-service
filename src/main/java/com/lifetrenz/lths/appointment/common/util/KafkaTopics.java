/**
 * 
 */
package com.lifetrenz.lths.appointment.common.util;

/**
 * @author Jess.B
 *
 */
public class KafkaTopics {

	public static final String TUMOR_BOARD_APPNT = "TUMOR_BOARD_APPNT";
	public static final String TUMOR_BOARD_MEETING_SCHEDULE = "TUMOR_BOARD_MEETING_SCHEDULE";
	public static final String TUMOR_BOARD_MEETING_DELETE = "TUMOR_BOARD_MEETING_DELETE";
	public static final String LT_AUDIT_TRIAL_EVENTS = "lt.audit.trail.events";

	public static final String UMS_USER_PROFILE_UPDATE = "UMS_USER_PROFILE_UPDATE";

	public static final String COMMITTEE_MEETING_SCHEDULE = "COMMITTEE_MEETING_SCHEDULE";
	public static final String COMMITTEE_MEETING_SCHEDULE_DELETE = "COMMITTEE_MEETING_SCHEDULE_DELETE";
	public static final String UPDATE_USER_IMAGE_URL = "UPDATE_USER_IMAGE_URL";
	public static final String SCHEDULED_PARTICIPANTS = "SCHEDULED_PARTICIPANTS";
	public static final String NOTIFICATION_REQUEST = "LT_NOTIFICATION_REQUEST";
	public static final String LT_APPOINTMENT_BOOK = "LT_APPOINTMENT_BOOK";
	public static final String CORE_APPOINTMENT_BOOK = "CORE_APPOINTMENT_BOOK";
	public static final String LTA_APPOINTMENT_CANCEL_TOPIC = "LTA_APPOINTMENT_CANCEL_TOPIC";
	public static final String LTA_APPOINTMENT_RESCHEDULE_TOPIC = "LTA_APPOINTMENT_RESCHEDULE_TOPIC";
	
	public static final String LTC_APPOINTMENT_CANCEL_TOPIC = "LTC_APPOINTMENT_CANCEL_TOPIC";
	public static final String LTC_APPOINTMENT_RESCHEDULE_TOPIC = "LTC_APPOINTMENT_RESCHEDULE_TOPIC";

	public static final String MOBILE_APPOINTMENT_SAVE_TOPIC = "onprem.opdservice.mobileappointmentsave.0";

	public static final String MOBILE_APPOINTMENT_RESCHEDULE_TOPIC = "onprem.opdservice.mobileappointmentrescedule.0";

	public static final String MOBILE_APPOINTMENT_CANCEL_TOPIC = "onprem.opdservice.mobileappointmentcancel.0";

	public static final String MOBILE_APPOINTMENT_CHECK_IN_TOPIC = "onprem.opdservice.mobileappointmentcheckin.0";
	
	public static final String UPDATE_CLINICAL_SCHEDULE_STATUS = "lt.update.schedule.status";
	
	public static final String LTA_TELECONSULT_DETAILS = "LTA_TELECONSULT_DETAILS";
	public static final String MARK_ADMISSION_DASH_BOARD_EVENT = "MARK_ADMISSION_DASH_BOARD_EVENT";
	public static final String LT_APPOINTMENT_STATUS = "lt.appointment.status";
	public static final String LT_APPOINTMENT_CREATE = "lt.appointment.create";
	public static final String LTM_APPOINTMENT_BOOK = "LTM_APPOINTMENT_BOOK";
	public static final String LT_CLINICAL_ADMISSION_STATUS = "lt.clinical.admission.status";
	public static final String LT_APPOINTMENT_SERVICE = "lt.appointment.service";
	
	public static final String LT_RECONFIRM_SERVICE = "lt.reconfirm.service";
	public static final String TUMOR_BOARD_MEETING_UPDATE = "TUMOR_BOARD_MEETING_UPDATE";
	public static final String LT_APPOINTMENT_CLINICAL_CREATE = "lt.appointment.clinical.create";

	public static final String APPOINTMENT_CLINICAL_EVENT = "APPOINTMENT_CLINICAL_EVENT";

		
	public static final String LT_AUDIT_LOGIN_AND_OUT_EVENT = "lt.audit.login.and.out.event";
	public static final String RELAY_APPOINTMENT_EVENTS = "RELAY_APPOINTMENT_EVENTS";

	public static final String LT_AUDIT_APPOINMENT_EVENT = "lt.audit.appoinment.event";
	
	public static final String LT_OPD_EVENTS = "lt.opd.events";
	
	public static final String LT_UMS_DELETE_USER ="LT_UMS_DELETE_USER";
	
	public static final String COMMITTEE_MEETING_SCHEDULE_PARTICIPANT_UPDATE = "COMMITTEE_MEETING_SCHEDULE_PARTICIPANT_UPDATE";
	
	public static final String LT_NOTIFICATION_EVENT = "lt.notification.events";
	
	public static final String CLINICAL_EVENTS = "lt.clinical.events";

	public static final String LT_SCHEDULE_EVENT = "lt.schedule.events";
	
	public static final String LT_APPOINTMENT_EVENT = "lt.appointment.event";
}