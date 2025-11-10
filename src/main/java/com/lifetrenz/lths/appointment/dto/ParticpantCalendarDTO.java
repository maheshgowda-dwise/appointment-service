package com.lifetrenz.lths.appointment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.model.value_object.DoctorGeneralDetails;
import com.lifetrenz.lths.appointment.model.value_object.Name;
import com.lifetrenz.lths.appointment.model.value_object.PatientGeneralDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticpantCalendarDTO {

	private String participantId;

	private String participantName;
	
	private String specialityIdentifier;

	private String appointmentParticipantType;

	private PatientGeneralDetails patientDetails;

	private DoctorGeneralDetails doctorDetails;

	private Name name;

	private String salutationName;
	
	private String aliasName;

}
