/**
 * 
 */
package com.lifetrenz.lths.appointment.repository.command;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.lifetrenz.lths.appointment.model.collection.CustomerGroup;

/**
 * @author Jess.B
 *
 */
public interface ICustomerGroupRepository extends CrudRepository<CustomerGroup, String>{
	
	List<CustomerGroup> findByExternalCustomerId(Long externalCustomerId);

}
