package com.lifetrenz.lths.appointment.model.enums;

public enum BlockStatus {
	BLOCKED("BLOCKED"), UNBLOCKED("UNBLOCKED");

	public final String value;

	/**
	 * @param string
	 */
	BlockStatus(String string) {
		value = string;
	}
}
