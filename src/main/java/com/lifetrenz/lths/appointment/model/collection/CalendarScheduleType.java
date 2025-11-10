/**
 * 
 */
package com.lifetrenz.lths.appointment.model.collection;

/**
 * @author Ajith.K
 *
 */
public enum CalendarScheduleType {

	AVAILABILITY("AS"), NON_AVAILABILITY("NS"), ADHOC("ADS"),APPOINTMENT("APT");

	public String value;

	CalendarScheduleType(String value) {
		this.value = value;
	}
}
