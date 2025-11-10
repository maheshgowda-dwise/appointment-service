package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientDetailsDTO {

	private String patientId;

	private String patientName;

	private String email;

	private Date dob;

	private String gender;
	
	private String mobilieNo;

}
