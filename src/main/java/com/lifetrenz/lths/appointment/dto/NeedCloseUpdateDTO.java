package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Pratim.S
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeedCloseUpdateDTO {
	private Long appointmentId;
	private String referenceAppointmentId;
	private String updatedBy;
	private Date updatedOn;
	private Long journeyStatusId;
	private String journeyStatusIdentifier;
	private String journeyStatus;
}
