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
public class AdmissionDashboardStatusDTO {

	private Long appointmentId;

	private Long admissionId;

	private Long encounterId;

	private ClinicalSystemMasterDTO admitStatus;

	private ClinicalSystemMasterDTO journeyStatus;

	private ClinicalSystemMasterDTO appointmentStatus;

	private Date eventActionDate;

	private EventActionType type;

	private String updatedBy;
	
	private ClinicalSystemMasterDTO encounterStatus;
	
	private String referenceAppointmentId;
	
	private String teleconsultChannelId;

}
