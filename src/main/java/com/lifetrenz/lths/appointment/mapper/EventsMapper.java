package com.lifetrenz.lths.appointment.mapper;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifetrenz.lths.appointment.common.enums.MessageEventStatus;
import com.lifetrenz.lths.appointment.common.enums.MessageRequestStatus;
import com.lifetrenz.lths.appointment.dto.CustomerTransactionAttributeDTO;
import com.lifetrenz.lths.appointment.model.events.MessageEvent;

public class EventsMapper {

	private static final Logger logger = LoggerFactory.getLogger(EventsMapper.class);

	public MessageEvent convertToMessageEvent(String request, Integer key, String topic, 
			CustomerTransactionAttributeDTO transactionBase, String username) {

		MessageEvent messageEvent = new MessageEvent();

		messageEvent.setEventStatus(MessageEventStatus.INPROGRESS);
		messageEvent.setKey(key);
		messageEvent.setTopic(topic);
		messageEvent.setRequest(request);
		messageEvent.setTransactionBase(transactionBase);
		return messageEvent;
	}
	
	public MessageEvent convertToMessageEvent(CustomerTransactionAttributeDTO transactionBase, Object request, Integer key, String topic, MessageRequestStatus status, String path, MessageEventStatus eventStatus, String errorMsg) {
        try {
            transactionBase = initializeTransactionBase(transactionBase);

            MessageEvent messageEvent = new MessageEvent();
            messageEvent.setTransactionBase(transactionBase);
            messageEvent.setEventStatus(eventStatus != null ? eventStatus : MessageEventStatus.INPROGRESS);
            messageEvent.setKey(key);
            messageEvent.setTopic(topic);
            messageEvent.setRequest(request);
            messageEvent.setStatus(status);
            messageEvent.setSource(path);
            messageEvent.setErrorMessage(errorMsg);

            return messageEvent;
        } catch (Exception e) {
            logger.error("Failed to convert to MessageEvent: {}", e.getMessage(), e);
            throw new RuntimeException("Error converting to MessageEvent", e);
        }
    }

    private CustomerTransactionAttributeDTO initializeTransactionBase(CustomerTransactionAttributeDTO transactionBase) {
        if (transactionBase == null) {
            transactionBase = new CustomerTransactionAttributeDTO();
        }
        transactionBase.setCreatedOn(new Date());
        return transactionBase;
    }
	
}
