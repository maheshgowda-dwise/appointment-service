package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaitingAppointmentDto {
	
	private String appointmentId;
	
	private Date startDate;
	
	private Date endDate;
	
	private Boolean isWaiting;

}
