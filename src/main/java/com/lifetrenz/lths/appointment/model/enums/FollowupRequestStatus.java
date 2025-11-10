package com.lifetrenz.lths.appointment.model.enums;

public enum FollowupRequestStatus {
	
	PENDING("PENDING"), COMPLETED("COMPLETED");

	public final String value;

	/**
	 * @param string
	 */
	FollowupRequestStatus(String string) {
		value = string;
	}

}
