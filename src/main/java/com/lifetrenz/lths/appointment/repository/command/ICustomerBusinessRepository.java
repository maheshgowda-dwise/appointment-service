/**
 * 
 */
package com.lifetrenz.lths.appointment.repository.command;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.lifetrenz.lths.appointment.model.collection.CustomerBusiness;

/**
 * @author Jess.B
 *
 */
public interface ICustomerBusinessRepository extends CrudRepository<CustomerBusiness, String> {

	List<CustomerBusiness> findByExternalCustomerBusinessId(Long externalCustomerBusinessId);
}
