package com.lifetrenz.lths.appointment.dto;

public enum EventAction {
	ADD("ADD"),UPDATE("UPDATE");
	public final String value;

	/**
	 * 
	 * @param i
	 */
	EventAction(String i) {
		this.value = i;
	}
}
