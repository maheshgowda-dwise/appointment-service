package com.lifetrenz.lths.appointment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.enums.NotificationRequestType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationRequestDTO {
	
	private NotificationRequestType requestType;
	
	private BookAppointmentNotificationDTO bookingAppointmentNotificationDTO;
	
	private MarkArriveNotificationDTO markArriveNotificationDTO;
	

}
