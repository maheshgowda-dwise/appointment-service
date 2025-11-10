/**
 * 
 */
package com.lifetrenz.lths.appointment.common.enums;

/**
 * @author Ajith.K
 *
 */
public enum ClinicalScheduleStatus {

	PENDING("PENDING"), SCHEDULED("SCHEDULED"), CANCELLED("CANCELLED"), FINAL_SCHEDULE("FINAL-SCHEDULE"),
	CONDUCTED("CONDUCTED");

	public final String value;

	/**
	 * @param i
	 */
	ClinicalScheduleStatus(String i) {
		this.value = i;
	}
}
