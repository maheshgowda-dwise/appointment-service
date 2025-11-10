package com.lifetrenz.lths.appointment.enums;

public enum ScheduleHolidayType {
	ADD_SCHEDULE_HOLIDAY("ADD_SCHEDULE_HOLIDAY"),UPDATE_SCHEDULE_HOLIDAY("UPDATE_SCHEDULE_HOLIDAY"),
	DELETE_SCHEDULE_HOLIDAY("DELETE_SCHEDULE_HOLIDAY");
public final String value;
	
	/**
	 * @param string
	 */
	ScheduleHolidayType(String string) {
		value = string;
	}
}
