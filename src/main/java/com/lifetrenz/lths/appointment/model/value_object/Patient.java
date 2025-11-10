package com.lifetrenz.lths.appointment.model.value_object;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

	private Long id;

	private Person person;

	private String mpi;

	private String externalMpi;

	private MaritalStatus maritalStatus;

	private EducationLevel educationLevel;

	private Date dateOfMarriage;

	private Set<IdentityDocument> identityDocument;

	private Boolean isVip;

	private Boolean isMaternity;

	private Boolean isTemp;

}
