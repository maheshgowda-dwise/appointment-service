package com.lifetrenz.lths.appointment.dto;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Mujaheed.N
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LTCSCancelAppointmentDTO {

	private Long appointmentId;

	private String externalAppointmentId;

	@NotNull
	private String cancellationReason;

	private String reasons;
}
