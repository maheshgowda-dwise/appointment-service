/**
 * 
 */
package com.lifetrenz.lths.appointment.model.collection;

import jakarta.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.lifetrenz.lths.appointment.model.License;
import com.lifetrenz.lths.appointment.model.value_object.Address;
import com.lifetrenz.lths.appointment.model.value_object.CustomerContact;
import com.lifetrenz.lths.appointment.model.value_object.TransactionBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Ajith.K
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customer_business")
public class CustomerBusiness extends TransactionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	@NotNull
	private String customerId;
	
	@NotNull
	private Long externalCustomerId;
	
	@NotNull
	private Long externalCustomerBusinessId;
	
	@NotNull
	private String name;
	
	private Address address;
	
	private CustomerContact primarycontact;
	
	private CustomerContact secondarycontact;
	
	private License license;

	

}
