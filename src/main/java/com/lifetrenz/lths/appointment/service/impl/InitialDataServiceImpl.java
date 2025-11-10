/**
 * 
 */
package com.lifetrenz.lths.appointment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.service.ActionComponentService;
import com.lifetrenz.lths.appointment.service.InitialDataService;

/**
 * @author Ajith.K
 *
 */
@Component
public class InitialDataServiceImpl implements InitialDataService {

	@Autowired
	ActionComponentService actionComponentService;
//
//	@Autowired
//	IGatewayRepository gatewayRepository;

	@Override
	public void addDefaultValues() throws ApplicationException {

		ObjectMapper mapper = new ObjectMapper();

//		try {
//			ClassPathResource resource = new ClassPathResource("action_components.json");
//			InputStream inputStream = resource.getInputStream();
//			String content = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
//			List<ActionComponent> actionComponents = Arrays
//					.asList(mapper.readValue(content, ActionComponent[].class));
//
//			try {
//				this.actionComponentService.addAllActionComponents(actionComponents);
//			} catch (ApplicationException e) {
//				throw new FailedException(e.getLocalizedMessage());
//			}
//		} catch (IOException e) {
//		}

		// add gateway code will come here

	}

}
