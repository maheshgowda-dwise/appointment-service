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
public class TransactionBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean active;

	private String createdBy;

	private Long createdById;

	private Date createdOn;

	private String updatedBy;

	private Long updatedById;

	private Date updatedOn;

}
