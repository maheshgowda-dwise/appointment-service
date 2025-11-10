package com.lifetrenz.lths.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewTelecomDTO {

	private String countryCode;
	private String number;
	private String telecomTypeIdentifier;

}
