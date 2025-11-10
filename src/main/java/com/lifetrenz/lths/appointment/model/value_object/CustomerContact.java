package com.lifetrenz.lths.appointment.model.value_object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerContact {

	private Name name;
	private Telecom mobile;
	private String email;
	private String username;
	private String password;
	
}
