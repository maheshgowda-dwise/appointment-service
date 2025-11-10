/**
 * 
 */
package com.lifetrenz.lths.appointment.repository.command;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lifetrenz.lths.appointment.model.collection.CustomerBusinessSite;

/**
 * @author Jess.B
 *
 */
public interface ICustomerBusinessSiteRepository extends MongoRepository<CustomerBusinessSite, String> {

	List<CustomerBusinessSite> findByCustomerBusinessId(String customerBusinessId);
}
