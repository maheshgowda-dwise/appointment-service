package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentRescheduleKafkaRequest implements Serializable {

	private static final long serialVersionUID = 606444252934032547L;

	private String specialityCode;
	private String doctorCode;
	private String slotId;
	private String mrn;
	private String appointmentNo;
	private String updatedBy;

}
