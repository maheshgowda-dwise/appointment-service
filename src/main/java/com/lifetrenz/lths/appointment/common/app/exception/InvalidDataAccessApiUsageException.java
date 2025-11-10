/**
 * 
 */
package com.lifetrenz.lths.appointment.common.app.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Ajith.K
 *
 */
public class InvalidDataAccessApiUsageException extends ApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5423547818571579885L;

	public InvalidDataAccessApiUsageException(String message) {
		super(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
	}
}
