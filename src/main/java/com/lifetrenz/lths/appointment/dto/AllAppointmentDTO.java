package com.lifetrenz.lths.appointment.dto;

import java.util.Date;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllAppointmentDTO {

	private Long appointmentId;

	private String patientName;

	private Long personId;

	private List<ParticipantCalendarShowDTO> participant;

	private String mpi;

	private Long genderId;

	private String genderName;

	private Date dateOfbirth;

	private TelecomDTO telecom;

	private Long appointmentUserId;

	private String appointmentUserName;

	private String startDate;

	private String endDate;

	private Long appointmentStatusId;

	private String appointmentStatusIdentifier;

	private String appointmentBookingSource;

	private String appointmentStatus;

	private String instructions;

	private String administrativeRemarks;

	private String reconfirmedReason;

	private Date reconfirmedOn;

	private String cancelledReason;

	private String patientProfilePhoto;
	
	private Long siteId;
	
	
}
