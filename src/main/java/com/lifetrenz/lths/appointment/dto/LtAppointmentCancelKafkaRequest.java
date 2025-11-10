package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LtAppointmentCancelKafkaRequest implements Serializable {

	private static final long serialVersionUID = 351489082927129324L;

	private String appointmentId;
	private String externalAppointmentId;
	private String status;
	private String remarks;
	private String reasonCode;
	private String updatedBy;
	private Long patientId;
	private TokenPayLoad tokenPayLoad;
	private Boolean isReschedule;

}
