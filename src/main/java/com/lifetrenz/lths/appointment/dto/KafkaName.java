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
public class KafkaName implements Serializable {

	private static final long serialVersionUID = 5917839181780690638L;

	public String firstname;

	public String middlename;

	public String lastname;

}
