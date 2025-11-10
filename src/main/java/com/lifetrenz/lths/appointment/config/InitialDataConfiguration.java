/**
 * 
 */
package com.lifetrenz.lths.appointment.config;

import org.springframework.beans.factory.annotation.Autowired;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.service.InitialDataService;

import jakarta.annotation.PostConstruct;

/**
 * 
 * @author Ajith.K
 *
 */
@org.springframework.context.annotation.Configuration
public class InitialDataConfiguration {

	@Autowired
	InitialDataService initialDataService;

	@PostConstruct
	public void initialDataLoad() throws ApplicationException {
		this.initialDataService.addDefaultValues();
	}

}
