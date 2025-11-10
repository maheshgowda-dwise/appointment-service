package com.lifetrenz.lths.appointment.model.enums;

public enum SlotCategory {
	INPERSON("INPERSON"), VIDEO("VIDEO");

	public final String value;

	/**
	 * @param string
	 */
	SlotCategory(String string) {
		value = string;
	}
}
