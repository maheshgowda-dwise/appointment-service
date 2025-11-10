/**
 * 
 */
package com.lifetrenz.lths.appointment.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.common.app.exception.FailedException;
import com.lifetrenz.lths.appointment.common.enums.MessageEventStatus;
import com.lifetrenz.lths.appointment.dto.CustomerTransactionAttributeDTO;
import com.lifetrenz.lths.appointment.dto.KafkaTopicsDto;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.mapper.EventsMapper;
import com.lifetrenz.lths.appointment.model.events.MessageEvent;
import com.lifetrenz.lths.appointment.repository.command.MessageEventRepository;
import com.lifetrenz.lths.appointment.repository.query.AppointmentRepository;
import com.lifetrenz.lths.appointment.service.MessageEventService;

/**
 * @author Ajith.K
 *
 */
@Component
public class MessageEventServiceImpl implements MessageEventService {

	@Autowired
	MessageEventRepository messageEventRepository;

	@Autowired
	AppointmentRepository appointmentRepository;

	@Autowired
	EventsMapper eventsMapper;

	@Override
	public void saveEvent(MessageEvent messageEvent) throws ApplicationException {

		try {
			this.messageEventRepository.save(messageEvent);
		} catch (Exception e) {
			throw new RuntimeException("Failed to save message event", e);
		}

	}

	@Override
	public MessageEvent getEventByKeyAndTopic(Integer key, String topic) throws ApplicationException {

		List<MessageEvent> result = this.messageEventRepository.findByKeyAndTopic(key, topic);

		if (result == null || result.isEmpty()) {
			throw new RuntimeException("No message event found for key: " + key + " and topic: " + topic);
		}

		return result.get(0);
	}

	@Override
	public void updateMessageStatus(Integer key, String topic, MessageEventStatus messageEventStatus)
			throws ApplicationException {
		try {
			MessageEvent messageEvent = getEventByKeyAndTopic(key, topic);

			if (messageEvent == null) {
				throw new RuntimeException("Message event not found for key: " + key + " and topic: " + topic);
			}

			messageEvent.setEventStatus(messageEventStatus);
			CustomerTransactionAttributeDTO trans = new CustomerTransactionAttributeDTO();
			trans.setUpdatedOn(new Date());
			messageEvent.setTransactionBase(trans);

			saveEvent(messageEvent);
		} catch (Exception e) {
			throw new RuntimeException("Failed to update message status", e);
		}
	}

	@Override
	public List<MessageEvent> getAllFailedEvents(Long startDate, Long endDate, String serviceName, String topicName)
			throws Exception {
		List<MessageEvent> getFailedMsgList = this.appointmentRepository.getDetailedFailedEvents(startDate, endDate,
				serviceName, topicName);
		return getFailedMsgList;
	}

	@Override
	public void saveEvent(Integer key, String topic, MessageEventStatus messageEventStatus, TokenPayLoad tokenPayload,
			Object request, String errorMessage) throws ApplicationException {
		MessageEvent messageEvent = new MessageEvent(null, null, key, topic, request, messageEventStatus, errorMessage,
				null, null);
		CustomerTransactionAttributeDTO trans = new CustomerTransactionAttributeDTO();

		trans.setActive(true);
		trans.setCustomerBusinessId(tokenPayload == null ? null : tokenPayload.getCustomerBusinessId());
		trans.setCustomerId(tokenPayload == null ? null : tokenPayload.getCustomerId());
		trans.setCreatedOn(new Date());
//		messageEvent.setActive(true);
//		messageEvent.setCustomerBusinessId(tokenPayload == null ? null : tokenPayload.getCustomerBusinessId());
//		messageEvent.setCustomerId(tokenPayload == null ? null : tokenPayload.getCustomerId());
//		messageEvent.setCreatedOn(new Date());
		messageEvent.setTransactionBase(trans);
		saveEvent(messageEvent);
	}

	@Override
	public List<KafkaTopicsDto> getKafkaTopics() throws ApplicationException {
		Properties config = new Properties();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Kafka server

		List<KafkaTopicsDto> res = new ArrayList<>();

		try (AdminClient adminClient = AdminClient.create(config)) {
			ListTopicsResult topics = adminClient.listTopics();
			Set<String> topicNames = topics.names().get(); // Get topic names

			for (String name : topicNames) {
				KafkaTopicsDto topicName = new KafkaTopicsDto();
				topicName.setName(name);
				res.add(topicName); // Add topic names to the list
			}

		} catch (Exception e) {
			throw new FailedException(e.getMessage());
		}
		return res;
	}

}
