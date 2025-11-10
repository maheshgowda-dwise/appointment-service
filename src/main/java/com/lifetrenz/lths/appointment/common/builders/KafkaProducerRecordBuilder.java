/**
 * 
 */
package com.lifetrenz.lths.appointment.common.builders;

import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @author Ajith.K
 *
 */
public class KafkaProducerRecordBuilder {


	public ProducerRecord<Integer, String> buildScheduledParticipant(Integer key, String request, String topic) {
		ProducerRecord<Integer, String> producerRecord = new ProducerRecord<Integer, String>(topic, key, request);
		return producerRecord;
	}
	
	
}
