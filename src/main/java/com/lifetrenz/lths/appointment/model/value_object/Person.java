package com.lifetrenz.lths.appointment.model.value_object;

import java.util.Date;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

	private Long id;

	private Name name;
	
	public String aliasName;

	private Gender gender;

	private Date dob;

	private Boolean isEstimatedDob;

	private List<PersonContact> personContact;

	private Set<PersonLanguage> languages;

	private String profilePhotoUrl;

	private Country nationality;

}
