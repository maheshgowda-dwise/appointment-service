package com.lifetrenz.lths.appointment.dto;

import java.util.List;

import com.lifetrenz.lths.appointment.model.collection.ScheduledParticipant;
import com.lifetrenz.lths.appointment.model.value_object.SystemMasterNew;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParticipantScheduleResponseDto {

	private String id;

	private SystemMasterNew calendarType;

	private SystemMasterNew participantType;

	private String conductingSiteId;

	private String participantId;

	private String participantName;

	private Long maximumWaitingList;

	private Boolean isActive;

	private Long duration;

	private String scheduleConfig;

	private String slotType;

	private CustomScheduleDto customScheduleDto;

	private List<ConfigBreakDto> configBreak;

	private ScheduledParticipant scheduledParticipant;

	private String status;

	private String reason;
	private SystemMasterNew visitType;

	private String conductingSiteName;
	private SystemMasterNew speciality;

}
