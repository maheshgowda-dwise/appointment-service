/**
 * 
 */
package com.lifetrenz.lths.appointment.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.model.collection.ActionComponent;
import com.lifetrenz.lths.appointment.repository.command.IActionComponentRepository;
import com.lifetrenz.lths.appointment.service.ActionComponentService;

/**
 * @author Ajith.K
 *
 */
@Component
public class ActionComponentServiceImpl implements ActionComponentService {

	@Autowired
	IActionComponentRepository actionComponentRepository;

	@Override
	public void addAllActionComponents(List<ActionComponent> actionComponents) throws ApplicationException {
		for (ActionComponent document : actionComponents) {
			if (this.actionComponentRepository.existsByIdentifierCode(document.getIdentifierCode())) {
				continue;
			}

			this.actionComponentRepository.save(document);
		}
	}

	@Override
	public ActionComponent updateActionComponentRoles(String[] roles, TokenPayLoad tokenPayload)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionComponent updateActionComponentUsers(String[] users, TokenPayLoad tokenPayload)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

}
