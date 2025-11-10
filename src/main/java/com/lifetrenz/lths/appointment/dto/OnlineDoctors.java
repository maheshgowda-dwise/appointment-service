package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;
import java.util.Collection;

import com.lifetrenz.lths.appointment.model.value_object.Name;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineDoctors implements Serializable{

	private static final long serialVersionUID = 6998670954973481357L;

	private String id;

	private Name name;

	private Registration registration;

	private String gender;

	private Specialization specialization;

	private String yearsOfExpirence;

	private String professionalStatement;

	private Collection<Qualification> qualifications;

	private String[] consultingHospitals;

	private String[] knownLanguages;

}
