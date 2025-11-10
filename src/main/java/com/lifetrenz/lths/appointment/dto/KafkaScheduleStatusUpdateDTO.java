package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KafkaScheduleStatusUpdateDTO {

	private String id;
	
	private Date ScheduledOn;
	
	private Long coreAppointmentId;
	
	private String userName;
	
	private String appointmentCategory;
	
	private Date startDate;

	private Date endDate;
	
	private ServiceAppointmentDetailsDTO serviceAppointment;
	
	private String scheduleStatus;
	
	private String clinicalOrderId;
	
	private String slotId;
}
