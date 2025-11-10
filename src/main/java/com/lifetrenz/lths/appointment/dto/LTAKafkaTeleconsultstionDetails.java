/**
 * 
 */
package com.lifetrenz.lths.appointment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LTAKafkaTeleconsultstionDetails {
	private String channelId;
	private String admissionId;
	private String externalVisitId;
	private String appointmentId;
	private String externalAppointmentId;
	private String referenceAppointmentId;
}
