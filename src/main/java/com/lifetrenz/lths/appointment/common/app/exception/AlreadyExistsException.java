package com.lifetrenz.lths.appointment.common.app.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AlreadyExistsException extends ApplicationException{

	private static final long serialVersionUID = -1829326050293295609L;

	public AlreadyExistsException(String message) {
		super(HttpStatus.CONFLICT.value(), message);
	}

}
