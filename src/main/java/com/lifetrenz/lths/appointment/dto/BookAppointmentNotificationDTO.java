package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookAppointmentNotificationDTO {

	private String appointmentId;

	private PatientDetailsDTO patientDetails;

	private DoctorDetailsDTO doctorDetails;

	private Long siteId;

	private Long customerBussinessId;

	private Long customerId;

	private String siteName;

	private Long userId;

	private String userName;

	private Boolean isReschedule;

	private Long departmentId;

	private String roleIdentifier;
	
	private Date firstAppointmentDate;

}
