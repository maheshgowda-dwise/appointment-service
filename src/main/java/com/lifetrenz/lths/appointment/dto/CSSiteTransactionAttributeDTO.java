/**
 * 
 */
package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class CSSiteTransactionAttributeDTO {

	private Boolean active;

	private Long createdBy;

	private Date createdOn;

	private Long updatedBy;

	private Date updatedOn;

	@NotNull
	private Long customerBusinessId;

	@NotNull
	private Long customerId;

	@NotNull
	private Long siteId;



}
