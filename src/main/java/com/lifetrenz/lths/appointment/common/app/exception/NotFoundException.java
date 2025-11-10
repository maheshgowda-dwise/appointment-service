package com.lifetrenz.lths.appointment.common.app.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApplicationException {

	private static final long serialVersionUID = -7264851570856163536L;

	public NotFoundException(String message) {
		super(HttpStatus.NOT_FOUND.value(), message);
	}

	public NotFoundException(String message, Throwable cause) {
		super(HttpStatus.NOT_FOUND.value(), message);
		initCause(cause);
	}

}
