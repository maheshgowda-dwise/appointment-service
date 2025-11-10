/**
 * 
 */
package com.lifetrenz.lths.appointment.dto;

import com.lifetrenz.lths.appointment.model.collection.CustomerBusiness;
import com.lifetrenz.lths.appointment.model.collection.CustomerGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jess.B
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCombined {

	private CustomerGroup customerGroup;

	private CustomerBusiness customerBusiness;

}
