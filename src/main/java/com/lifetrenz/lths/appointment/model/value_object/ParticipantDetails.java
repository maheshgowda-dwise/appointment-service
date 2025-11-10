package com.lifetrenz.lths.appointment.model.value_object;

import java.io.Serializable;

import com.lifetrenz.lths.appointment.dto.SystemMasterDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDetails implements Serializable{

	private static final long serialVersionUID = 496472612313972554L;
	
	private String participantId;

	private String participantName;
	
	private String specialityIdentifier;

	private SystemMasterDTO appointmentParticipantType;
	
	private PatientGeneralDetails patientDetails;
	
	private DoctorGeneralDetails doctorDetails;
	
	private Name name;
	
	private String salutationName;
	
	private String aliasName;

}
