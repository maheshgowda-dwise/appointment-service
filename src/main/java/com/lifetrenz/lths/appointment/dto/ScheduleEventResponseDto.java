package com.lifetrenz.lths.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleEventResponseDto {
	
	private String scheduleEventId; 
	private String participantId;
	private String referrenceId;

}
