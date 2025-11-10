/**
 * 
 */
package com.lifetrenz.lths.appointment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.common.enums.MessageEventStatus;
import com.lifetrenz.lths.appointment.dto.KafkaTopicsDto;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.model.events.MessageEvent;

/**
 * @author Ajith.K
 *
 */
@Service
public interface MessageEventService {

	/**
	 * 
	 * @param messageEvent
	 * @throws ApplicationException
	 */
	void saveEvent(MessageEvent messageEvent) throws ApplicationException;

	/**
	 * 
	 * @param key
	 * @param topic
	 * @return
	 * @throws ApplicationException
	 */
	MessageEvent getEventByKeyAndTopic(Integer key, String topic) throws Exception;

	/**
	 * 
	 * @param key
	 * @param topic
	 * @param messageEventStatus
	 * @throws ApplicationException
	 */
	void updateMessageStatus(Integer key, String topic, MessageEventStatus messageEventStatus) throws Exception;

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param serviceName
	 * @param topicName
	 * @return
	 * @throws Exception
	 */
	public List<MessageEvent> getAllFailedEvents(Long startDate, Long endDate, String serviceName, String topicName)
			throws Exception;

	/**
	 * To save kafka error event
	 * 
	 * @param key
	 * @param topic
	 * @param messageEventStatus
	 * @param tokenPayload
	 * @param request
	 * @param errorMessage
	 * @throws ApplicationException
	 */
	void saveEvent(Integer key, String topic, MessageEventStatus messageEventStatus, TokenPayLoad tokenPayload,
			Object request, String errorMessage) throws ApplicationException;
	
	
	/**
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	public List<KafkaTopicsDto> getKafkaTopics()throws ApplicationException, ApplicationException;

}
