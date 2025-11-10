/**
 * 
 */
package com.lifetrenz.lths.appointment.common.enums;

/**
 * @author Ajith.K
 *
 */
public enum KafkaTopic {

	TUMOR_BOARD_APPNT(1), SCHEDULED_PARTICIPANTS(2), BOOK_APPOINTMENT_NOTIFICATION_REQUEST(3), NOTIFICATION_REQUEST(4),
	LT_NOTIFICATION_EVENT(5), LT_SCHEDULE_EVENT(6), LT_APPOINTMENT_EVENT(7);

	public final int value;

	/**
	 * @param i
	 */
	KafkaTopic(int i) {
		value = i;
	}

}
