/**
 * 
 */
package com.lifetrenz.lths.appointment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.model.collection.ActionComponent;

/**
 * @author Ajith.K
 *
 */
@Service
public interface ActionComponentService {

	/**
	 * 
	 * @param actionComponents
	 */
	void addAllActionComponents(List<ActionComponent> actionComponents) throws ApplicationException;

	/**
	 * 
	 * @param roles
	 * @param tokenPayload
	 * @return
	 * @throws ApplicationException
	 */
	ActionComponent updateActionComponentRoles(String[] roles, TokenPayLoad tokenPayload) throws ApplicationException;

	/**
	 * 
	 * @param users
	 * @param tokenPayload
	 * @return
	 * @throws ApplicationException
	 */
	ActionComponent updateActionComponentUsers(String[] users, TokenPayLoad tokenPayload) throws ApplicationException;
}
