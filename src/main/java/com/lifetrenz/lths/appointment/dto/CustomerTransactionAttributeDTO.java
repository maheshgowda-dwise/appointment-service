/**
 * 
 */
package com.lifetrenz.lths.appointment.dto;

import java.util.Date;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerTransactionAttributeDTO {
	
	private Boolean active;
	
	private Long createdBy;
	
	private Date createdOn; 
	
	private Long updatedBy;
	
	private Date updatedOn;
	
	private Long customerBusinessId;
	
	private Long customerId;
	
	private Long siteId;
	
	private String createdByName;


}
