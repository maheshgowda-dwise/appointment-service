/**
 * 
 */
package com.lifetrenz.lths.appointment.service;

import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import com.lifetrenz.lths.appointment.model.events.MessageEvent;

/**
 * @author Ajith.K
 *
 */
@Service
public interface ProducerService {

/**
 * 
 * @param key
 * @param topic
 * @param request
 * @param messageEvent
 * @return
 * @throws Exception
 */
	ListenableFuture<SendResult<Integer, String>> publishToKafka(Integer key, String topic, String request, MessageEvent messageEvent)
			throws Exception ;

}
