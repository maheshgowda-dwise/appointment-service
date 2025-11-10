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
public class KafkaTransactionBase {

	private static final long serialVersionUID = 1L;

	private Long customerId;
	
	private Long customerBusinessId;
	
	private Boolean active;
	
	private Long siteId;
	
	private String createdBy;
	
	private Long createdById;
	
	private Date createdOn;
	
	private String updatedBy;
	
	private Long updatedById;
	
	private Date updatedOn;

	
}
