package com.lifetrenz.lths.appointment.common.enums;

public enum SlotStatus {
	
	AVAILABLE(1), ELAPSED(2), OCCUPIED(3), NOT_AVAILABLE(4);

	public final int value;
	
	private SlotStatus(int value) {
		this.value = value;
	}
}
