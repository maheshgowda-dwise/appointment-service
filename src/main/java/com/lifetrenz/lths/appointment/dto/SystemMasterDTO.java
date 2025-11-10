/**
 * 
 */
package com.lifetrenz.lths.appointment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotNull;
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
@JsonIgnoreProperties(ignoreUnknown =  true)
public class SystemMasterDTO {

	private Long id;

	@NotNull
	private String nameEn;

	private String description;

	@NotNull
	private String identifierCode;

}
