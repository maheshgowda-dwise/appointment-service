package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonNameMobDto implements Serializable{

	private static final long serialVersionUID = 2391833294382349136L;

	private String firstName;

	private String lastName;

	public String getFullName() {
		return String.format("%s %s", this.getFirstName(), this.getLastName());
	}

}
