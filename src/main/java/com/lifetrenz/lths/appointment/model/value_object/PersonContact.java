package com.lifetrenz.lths.appointment.model.value_object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonContact {

	private Long id;

	private Person person;

	private Address primaryAddress;

	private SecondaryAddress secondaryAddress;

	private Name name;

	private String email;

	private Telecom telecom;

}
