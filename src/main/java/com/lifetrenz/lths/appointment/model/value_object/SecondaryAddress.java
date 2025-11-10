package com.lifetrenz.lths.appointment.model.value_object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecondaryAddress {

	public String addressLine1;
	public String addressLine2;
	public String area;
	public Long cityId;
	public Long stateId;
	public Long countryId;
	public String zipcode;
	private String nationality;

}
