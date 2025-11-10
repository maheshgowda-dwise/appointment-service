/**
 * 
 */
package com.lifetrenz.lths.appointment.common.builders;

import java.util.Date;

import com.lifetrenz.lths.appointment.common.enums.MessageEventStatus;
import com.lifetrenz.lths.appointment.dto.CustomerTransactionAttributeDTO;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.model.events.MessageEvent;

/**
 * @author Ajith.K
 *
 */
public class MessageEventBuilder {

	public MessageEvent getMessageEvent(String request, Integer key, String topic, TokenPayLoad tokenPayload,
			Long siteId) {
		
		CustomerTransactionAttributeDTO customerTransactionAttributeDTO = new CustomerTransactionAttributeDTO();
		customerTransactionAttributeDTO.setCreatedBy(tokenPayload.getCoreUserId());
		customerTransactionAttributeDTO.setCustomerBusinessId(tokenPayload.getCustomerBusinessId());
		customerTransactionAttributeDTO.setCustomerId(tokenPayload.getCustomerId());
		customerTransactionAttributeDTO.setSiteId(siteId);
		customerTransactionAttributeDTO.setCreatedOn(new Date());
		
		
		MessageEvent messageEvent = new MessageEvent();
		messageEvent.setEventStatus(MessageEventStatus.INPROGRESS);
		messageEvent.setKey(key);
		messageEvent.setTopic(topic);
		messageEvent.setRequest(request);
		messageEvent.setTransactionBase(customerTransactionAttributeDTO);
		return messageEvent;
	}
}
