/**
 * 
 */
package com.lifetrenz.lths.appointment.repository.command;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lifetrenz.lths.appointment.model.events.MessageEvent;

/**
 * @author Ajith.K
 *
 */
public interface MessageEventRepository extends MongoRepository<MessageEvent, String> {

	List<MessageEvent> findByKeyAndTopic(Integer key, String topic);
	
	/**
	 * 
	 * @param paramInteger
	 * @return
	 */
	List<MessageEvent> findByKey(String paramInteger);

	/**
	 * 
	 * @param paramString
	 * @return
	 */
	List<MessageEvent> findByTopic(String paramString);
}
