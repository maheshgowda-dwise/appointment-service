/**
 * 
 */
package com.lifetrenz.lths.appointment.repository.command;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lifetrenz.lths.appointment.model.collection.ActionComponent;

/**
 * @author Ajith.K
 *
 */
public interface IActionComponentRepository extends MongoRepository<ActionComponent, String> {

	/**
	 * 
	 * @param identifierCode
	 * @return
	 */
	List<ActionComponent> findByIdentifierCode(String identifierCode);
	
	/**
	 * 
	 * @param identifierCode
	 * @return
	 */
	boolean existsByIdentifierCode(String identifierCode);
}
