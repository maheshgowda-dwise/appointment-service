package com.lifetrenz.lths.appointment.model.value_object;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Speciality {

	private Long id;

	@NotNull
	private String nameEn;

	private String description;

	@NotNull
	private String identifierCode;

}
