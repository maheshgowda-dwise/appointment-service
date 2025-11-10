package com.lifetrenz.lths.appointment.common.app.constant;


public class ExceptionConstants {

	public static final String ERROR = "ERROR";

	public static final String BAD_REQUEST_DEFAULT_MESSAGE = "There are unaccepted parameters";
	public static final String BAD_REQUEST_EXISTS_USER_MESSAGE = "This user already exists";

	private ExceptionConstants() {
		throw new IllegalStateException("Utility Class cannot be instantiated");
	}

}
