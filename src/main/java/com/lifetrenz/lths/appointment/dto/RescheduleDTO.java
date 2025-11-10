package com.lifetrenz.lths.appointment.dto;

import jakarta.validation.constraints.NotNull;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.model.collection.Slots;
import com.lifetrenz.lths.appointment.model.value_object.ClinicalActivityDetails;
import com.lifetrenz.lths.appointment.model.value_object.Remarks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RescheduleDTO {

	private Long appointmentId;

	private String externalAppointmentId;

	private String referenceAppointmentId;

	private Remarks remarks;

	@NotNull
	private Long appointmentDate;

	private String payerTypeIdentifier;

	private String instructions;

	private String administrativeNotes;

	private Slots slots;

	private String appointmentStatus;

	private Long scheduleId;

	private String appointmentServiceType;

	private ClinicalActivityDetails clinicalActivityDetails;

	private String username;

	private Long endDate;

	private String source;

}
