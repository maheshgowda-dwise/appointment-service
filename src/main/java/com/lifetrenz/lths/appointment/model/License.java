/**
 * 
 */
package com.lifetrenz.lths.appointment.model;

import java.util.Date;

import com.lifetrenz.lths.appointment.model.value_object.LicenseWarning;

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
public class License {

	private Long id;

	private String key;

	private String gracePeriod;

	private Date validtill;

	private LicenseWarning licenseWarning;

}
