package com.lifetrenz.lths.appointment.dto;


import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSlotUtilizationDTO {

	@Id
	private String id;

	private Long conductingSiteId;	

	private String conductingSiteName;
	
	private String 	participantId;	
	
	private String participantName;

	private String participantType;
	
	private String appointmentCategory;
	
	private String appointmentType;
	
	private String appointmentBookingSource;
	
	private Date startDate;
	
	private Date endDate;
	
	private String durationinMinutes;
	
	private String reconfirmedReason;
	
	private Long customerId;
	
	private Long customerBusinessId;
	
	private Date createdOn;
	
}

