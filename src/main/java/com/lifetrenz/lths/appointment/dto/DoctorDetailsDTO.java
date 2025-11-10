package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorDetailsDTO {

	private String doctorName;

	private Long coreAppointmentId;

	private String refDoctorId;

	private String refDoctorName;

	private Date appointmentStartDate;

	private Date appointmentEndDate;

	private String doctorId;
	
	private String doctorEmail;
	
	private String mobilieNo;
	
	private String salutationName;
}
