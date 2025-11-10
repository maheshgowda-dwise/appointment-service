package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LtRescheduleAppointmentDto implements Serializable {

	private static final long serialVersionUID = 7717152150576974361L;

	private String appointmentId;

	private String externalAppointmentId;

	private String remarks;

	private Long startTime;

	private Long endTime;

	private String slotId;

	private String patientId;

	private String doctorId;

	private String updatedBy;
	
	private Long appointmentToDate;
	private Long appointmentDate;
	
	private String id;


}
