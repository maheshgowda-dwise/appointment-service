package com.lifetrenz.lths.appointment.common.enums;

public enum SlotReservation {
	
	NO_RESERVATION(0), IN_PERSON(1), ONLINE(2), ANY(3);

	public final int value;
	
	private SlotReservation(int value) {
		this.value = value;
	}
}
