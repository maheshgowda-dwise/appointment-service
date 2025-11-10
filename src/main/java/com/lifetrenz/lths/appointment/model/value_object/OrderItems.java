package com.lifetrenz.lths.appointment.model.value_object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Mujaheed.N
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItems {

	private Long identifier;

	private String name;

	private String identifierCode;

	private String description;


}
