package com.lifetrenz.lths.appointment.model.value_object;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mobile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank
//	@Column(name = "country_code")
	private String countryCode;
	
	@NotBlank
//	@Column(name = "mobile_number")
	private String number;
	
	
}
