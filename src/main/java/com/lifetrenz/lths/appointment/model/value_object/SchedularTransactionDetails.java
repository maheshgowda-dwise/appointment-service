package com.lifetrenz.lths.appointment.model.value_object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchedularTransactionDetails {
	
	private String committeId;
	
	private String committeName;

}
