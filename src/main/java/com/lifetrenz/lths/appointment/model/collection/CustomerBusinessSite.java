/**
 * 
 */
package com.lifetrenz.lths.appointment.model.collection;

import jakarta.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.lifetrenz.lths.appointment.model.value_object.Address;
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
@Document(collection = "customer_business_site")
public class CustomerBusinessSite extends TransactionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@NotNull
	private String customerBusinessId;

	@NotNull
	private Long externalCustomerBusinessId;

	@NotNull
	private Long externalSiteId;

	@NotNull
	private String name;

	@NotNull
	private String aliasname;

	private Address address;

	private String region;

	private Boolean enableAppointmentConfig;

}
