/**
 * 
 */
package com.lifetrenz.lths.appointment.model.value_object.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ajith.K
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarType {

	private Long id;

	private String nameEn;

	private String description;

	private String identifierCode;
}
