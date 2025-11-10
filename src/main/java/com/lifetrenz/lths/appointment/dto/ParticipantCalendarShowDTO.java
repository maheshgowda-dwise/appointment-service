package com.lifetrenz.lths.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantCalendarShowDTO {

	private Long participantId;

	private String participantName;
	
	private String specialityIdentifier;

	private Long appointmentId;

	private String appointmentParticipantTypeId;

	private Long specializationId;

}
