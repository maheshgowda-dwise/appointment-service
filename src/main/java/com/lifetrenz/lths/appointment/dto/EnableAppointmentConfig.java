/**
 * 
 */
package com.lifetrenz.lths.appointment.dto;

import java.util.List;

import com.lifetrenz.lths.appointment.model.collection.CustomerBusinessSite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ajith.K
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnableAppointmentConfig {

	private Long customerId;

	private Long customerBusinessId;

	List<CustomerBusinessSite> customerBusinessSites;

	
}
