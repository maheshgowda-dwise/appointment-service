package com.lifetrenz.lths.appointment.model.value_object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaritalStatus {

	private Long id;

	private String name;

	private String description;

	private String identifierCode;

}
