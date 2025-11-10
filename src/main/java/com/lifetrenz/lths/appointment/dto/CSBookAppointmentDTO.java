/**
 * 
 */
package com.lifetrenz.lths.appointment.dto;

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
public class CSBookAppointmentDTO {

	private Long id;

	private CSSiteTransactionAttributeDTO siteTransactionAttribute;

	@NotNull
	private Long personId;

	@NotNull
	private Long appointmentUserId;

	private String appointmentServiceCategory;

	private Long appointmentTypeId;

	private String payerTypeIdentifier;

	private String visitTypeIdentifier;

	private Long appointmentPriorityId;

	@NotNull
	private Long startDate;

	@NotNull
	private Long endDate;

	private String durationinMinutes;

	private String instructions;

	private String administrativeNotes;

	private Boolean iswaitingList;

}
