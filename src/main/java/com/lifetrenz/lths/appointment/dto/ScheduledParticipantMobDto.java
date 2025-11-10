package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.model.value_object.Address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduledParticipantMobDto implements Serializable {

	private static final long serialVersionUID = 6736824201554868661L;

	private String doctorId;

	private PersonNameMobDto doctorName;

	private Registration registration;

	private String gender;

	private List<Specialization> specialization;

	private List<WorkExperienceDTO> yearsOfExpirence;

	private String professionalStatement;

	private List<Qualification> qualifications;

	private String[] knownLanguages;

	private String profilePhoto;

	private List<DoctorSiteMobDto> sites;

	private String nationality;

	private KafkaTransactionBase transactionBase;

	private String preference;

	private String designation;
	
	private Address address;
	
	private TelecomDTO telecom;
	
	private String email;

}
