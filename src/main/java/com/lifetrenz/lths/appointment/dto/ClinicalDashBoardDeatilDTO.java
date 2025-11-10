package com.lifetrenz.lths.appointment.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClinicalDashBoardDeatilDTO {

	private EncounterDetailDTO encounter;

	private AdmissionDetailDTO admission;

	private PatientDetailDTO patient;

	private AppointmentDetailDTO appointment;

	private Long createdById;

	private Long admissionSiteId;

}
