package com.lifetrenz.lths.appointment.common.enums;

public enum DayOfWeek {

	SUNDAY("SU"), MONDAY("MO"), TUESDAY("TU"), WEDNESDAY("WE"), THURSDAY("TH"), FRIDAY("FR"), SATURDAY("SA");

	public final String value;

	private DayOfWeek(String value) {
		this.value = value;
	}
}
