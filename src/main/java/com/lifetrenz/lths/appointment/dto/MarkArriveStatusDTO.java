package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarkArriveStatusDTO {
	private Long id;

	private EncounterDetailDTO encounter;

	private ClinicalSystemMasterDTO admitStatus;

	private ClinicalSystemMasterDTO journeyStatus;

	private Long appointmentId;

	private ClinicalSystemMasterDTO appointmentStatus;

	private Date arrivalTime;
	
	private String referenceAppointmentId;
	private Long startDate;
	private Long endDate;
	private String externalVisitId;
	private Boolean isHealthCheckScreen;

}
