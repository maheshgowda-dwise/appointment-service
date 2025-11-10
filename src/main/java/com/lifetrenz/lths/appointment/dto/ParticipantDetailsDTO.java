package com.lifetrenz.lths.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParticipantDetailsDTO {
	
	
	private PatientDetailsDTO patientDetails;

	private DoctorDetailsDTO doctorDetails;

}
