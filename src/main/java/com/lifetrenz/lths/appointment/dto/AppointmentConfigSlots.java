package com.lifetrenz.lths.appointment.dto;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentConfigSlots  {

	@Id
	private String id;

	//private SystemMasterDTO participantType;

	private String scheduleidentifier;

	private String schedculeType;

	private String participantId;

	private String participantName;

	private Long scheduleFrom;

	private Long scheduleTo;

	private String availableFromTime;

	private String availableToTime;

//	private SystemMasterDTO calendarType;

	private String[] days;

	private String duration;

	private String slotType;

}
