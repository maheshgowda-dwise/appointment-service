package com.lifetrenz.lths.appointment.common.app.exception;

import org.springframework.http.HttpStatus;

public class InvalidAccessException extends ApplicationException {

	private static final long serialVersionUID = -6314144853366421076L;

	public InvalidAccessException(String message) {
		super(HttpStatus.UNAUTHORIZED.value(), message);
	}

}
