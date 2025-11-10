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
public class AppointmentDetailDTO {

	private Long appointmentId;

	private String referenceAppointmentId;

	private ClinicalSystemMasterDTO appointmentStatus;

	private ClinicalSystemMasterDTO appointmentMode;

	private ClinicalSystemMasterDTO visitType;

	private Boolean isNewVisit;

	private String participantIdentifier;

	private String reconfirmedReason;

	private Date reconfirmedOn;

	private Boolean waitingList;

	private Date registrationDate;

	private String instructions;

	private String externalAppointmentId;
	
//	private List<PatientVisitNote> visitNotes;

	private ClinicalSystemMasterDTO appointmentCategory;
	
	private String mobilityAppointmentId;

}
