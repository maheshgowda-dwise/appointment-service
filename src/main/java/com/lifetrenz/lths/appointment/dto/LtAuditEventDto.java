package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LtAuditEventDto {

	private AuditEventType auditType;

	private String data;

	private String entityType;

	private String entityId;

	private EventAction eventAction;

	private Boolean active;

	private String createdBy;

	private Long createdById;

	private Date createdOn;

	private Long customerBusinessId;

	private Long customerId;

	private Long siteId;
	
	private String entityDisplayName;
	private String patientName;
	private String patientMPI;
	private String message;
}
