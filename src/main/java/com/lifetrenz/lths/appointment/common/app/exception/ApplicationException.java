package com.lifetrenz.lths.appointment.common.app.exception;

public abstract class ApplicationException extends Exception {

	private static final long serialVersionUID = -7957450782136807885L;
	
	private final int code;
	
	public ApplicationException(final int code, final String message) {
		super(message);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}

// Remove AppointmentMappingException from this file.
