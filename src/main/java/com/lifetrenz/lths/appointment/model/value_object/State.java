package com.lifetrenz.lths.appointment.model.value_object;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class State {

	private Long id;

	private Boolean active = true;

	private String name;

	private String state_code;

	private String type;

	private Float latitude;

	private Float longitude;

	private Country country;

	private Set<City> cities;


}
