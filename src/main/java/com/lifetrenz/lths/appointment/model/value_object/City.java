package com.lifetrenz.lths.appointment.model.value_object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class City {

	private Long id;

	private Boolean active = true;

	private String name;

	private Float latitude;

	private Float longitude;

	private String wiki_data_id;

	private State state;

	private Country country;
}
