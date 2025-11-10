package com.lifetrenz.lths.appointment.model.value_object;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//import com.lifetrenz.lths.pm.valueobjects.validators.ZipCodeValidator;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String addressLine1;
	public String addressLine2;
	public String area;
	public Long cityId;
	public Long stateId;
	public Long countryId;
	public String zipcode;
	private String nationality;


}
