package com.lifetrenz.lths.appointment.common.app.exception;

import org.springframework.http.HttpStatus;

public class ExternalServiceDependencyException extends ApplicationException {
	
	private static final long serialVersionUID = -7445703840850263494L;

	public ExternalServiceDependencyException(String message) {
		super(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
	}

}
