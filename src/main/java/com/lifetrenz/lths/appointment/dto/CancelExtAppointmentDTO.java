package com.lifetrenz.lths.appointment.dto;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CancelExtAppointmentDTO {

	private Long appointmentId;

	private String externalAppointmentId;
	
	private String referenceAppointmentId;

	@NotNull
	private String cancellationReason;
	
	private String reasonCode;

	private String userName;

	private String source;
	
	private Boolean isReschedule;
}
