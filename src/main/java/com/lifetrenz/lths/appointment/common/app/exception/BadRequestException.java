package com.lifetrenz.lths.appointment.common.app.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BadRequestException extends ApplicationException{

	private static final long serialVersionUID = -2118846901713997155L;

	public BadRequestException(String message) {
		super(HttpStatus.BAD_REQUEST.value(), message);
	}

}
