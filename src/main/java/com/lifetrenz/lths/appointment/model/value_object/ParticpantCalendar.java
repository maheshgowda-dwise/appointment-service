package com.lifetrenz.lths.appointment.model.value_object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticpantCalendar {

	private String participantId;

	private String participantName;
	
	private String specialityIdentifier;


	private AppointmentParticipantType appointmentParticipantType;

	private PatientGeneralDetails patientDetails;

	private DoctorGeneralDetails doctorDetails;

	private Name name;

	private String salutationName;
	 
	private String aliasName;

}
