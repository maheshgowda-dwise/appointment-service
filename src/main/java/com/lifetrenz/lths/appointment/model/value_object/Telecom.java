/**
 * 
 */
package com.lifetrenz.lths.appointment.model.value_object;

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
public class Telecom {

	private String countryCode;
	private String number;
	private String telecomTypeIdentifier;


}
