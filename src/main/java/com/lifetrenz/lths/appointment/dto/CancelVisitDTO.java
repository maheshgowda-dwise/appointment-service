package com.lifetrenz.lths.appointment.dto;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.model.value_object.Remarks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 
 * @author Pratham.C
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CancelVisitDTO {

	@NotNull
	private Remarks remarks;

	private Long appointmentDate;
	
	private Long personId;
	
	private String admissionId;
	
	private Long patientId;
	
	private Long siteId;
	
	private Long appointmentId;
	
	private String referenceAppointmentId;
	
	private Long encounterId;
	
	private Boolean active;

}