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
public class AppointmentParticipantType {

	
	private Long id;

	private String name;

	private String description;

	private String identifierCode;
	

}
