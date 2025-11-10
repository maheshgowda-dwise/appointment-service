package com.lifetrenz.lths.appointment.common.app.exception;

import org.springframework.http.HttpStatus;

public class NotAcceptableException extends ApplicationException {

	private static final long serialVersionUID = -2983719956425313356L;

	public NotAcceptableException(String message) {
		super(HttpStatus.NOT_ACCEPTABLE.value(), message);
	}

}
