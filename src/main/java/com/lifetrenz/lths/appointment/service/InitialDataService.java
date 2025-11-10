/**
 * 
 */
package com.lifetrenz.lths.appointment.service;

import org.springframework.stereotype.Service;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;

/**
 * @author Ajith.K
 *
 */
@Service
public interface InitialDataService {

	/**
	 * 
	 * @param postUserDTO
	 * @throws ApplicationException
	 */
	public void addDefaultValues() throws ApplicationException;

}
