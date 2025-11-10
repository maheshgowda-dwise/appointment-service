package com.lifetrenz.lths.appointment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdmissionDashBoardDTO {
	private String dashBoardId;

	private EncounterDetailDTO encounter;

	private AdmissionDetailDTO admission;

	private PatientDetailDTO patient;

	private AppointmentDetailDTO appointment;

	private TransactionDetailDTO transAttribute;

	private Long admissionSiteId;

	private InsuranceDetails insuranceDetails;

	private String tokenNumber;
	
	private Long bedBookingId;
	

}
