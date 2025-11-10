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
public class KafkaAppointmentStatusDto implements Serializable{

	private static final long serialVersionUID = 9045303587742311574L;
	
	private String referenceAppointmentId;

	private SystemMasterDTO apntStatus;

	private String updatedBy;

	private Long updatedById;

}
