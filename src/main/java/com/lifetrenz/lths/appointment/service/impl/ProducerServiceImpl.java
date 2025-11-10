/**
 * 
 */
package com.lifetrenz.lths.appointment.service.impl;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import com.lifetrenz.lths.appointment.common.builders.KafkaProducerRecordBuilder;
import com.lifetrenz.lths.appointment.common.enums.MessageEventStatus;
import com.lifetrenz.lths.appointment.dto.CustomerTransactionAttributeDTO;
import com.lifetrenz.lths.appointment.model.events.MessageEvent;
import com.lifetrenz.lths.appointment.service.ProducerService;

import io.github.resilience4j.retry.Retry;

/**
 * @author Ajith.K
 *
 */
@Component
public class ProducerServiceImpl implements ProducerService {

	final static Logger logger = LoggerFactory.getLogger(ProducerServiceImpl.class);

	@Autowired
	private KafkaTemplate<Integer, String> kafkaItemTemplate;

	@Autowired
	KafkaTemplate<Integer, String> kafkaTemplate;

	@Autowired
	KafkaProducerRecordBuilder kafkaProducerRecordBuilder;

	@Autowired
	com.lifetrenz.lths.appointment.service.MessageEventService messageEventService;

	@Autowired
	private Retry kafkaRetry;

	@Override
	public ListenableFuture<SendResult<Integer, String>> publishToKafka(Integer key, String topic, String request,
			MessageEvent messageEvent) throws Exception {

		ProducerRecord<Integer, String> producerRecord = kafkaProducerRecordBuilder.buildScheduledParticipant(key,
				request, topic);

		try {
			kafkaRetry.executeSupplier(() -> {
				kafkaItemTemplate.send(producerRecord);
				return null;
			});
			messageEventService.updateMessageStatus(key, topic, MessageEventStatus.SUCCESS);
		} catch (Exception e) {
			handleKafkaFailure(messageEvent, e);
			throw new Exception("Failed to publish message to Kafka", e);
		}

		return null;
	}

	private void handleKafkaFailure(MessageEvent messageEvent, Exception e) {
		logger.error("Kafka publish failed: {}", e.getMessage(), e);
		messageEvent.setEventStatus(MessageEventStatus.FAILURE);
		messageEvent.setErrorMessage(e.getMessage());

		CustomerTransactionAttributeDTO transactionAttributes = new CustomerTransactionAttributeDTO();
		transactionAttributes.setActive(true);
		messageEvent.setTransactionBase(transactionAttributes);

		try {
			messageEventService.saveEvent(messageEvent);
		} catch (Exception saveException) {
			logger.error("Failed to save message event: {}", saveException.getMessage(), saveException);
		}
	}

}