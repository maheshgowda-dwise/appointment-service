/**
 * 
 */
package com.lifetrenz.lths.appointment.model.value_object;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Remarks implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String remarksType;
	
	private String remarks;
	
	private Date createdOn;
	
	private String oldValue;
	
	private String newValue;

	
}
