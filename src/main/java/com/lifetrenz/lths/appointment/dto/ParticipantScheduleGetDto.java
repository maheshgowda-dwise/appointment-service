package com.lifetrenz.lths.appointment.dto;

import java.util.List;


import com.lifetrenz.lths.appointment.model.collection.ScheduledParticipant;
import com.lifetrenz.lths.appointment.model.value_object.SystemMasterNew;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantScheduleGetDto {
	
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

	private String scheduleType;

	private String reason;

	private Long maxWaitingPerSlot;

	private Long consumedWaitingPerSession;

	private Long consumedWaitingPerSlot;
	
	private Boolean blockWaitingSession;
	
	private Boolean blockWaitingSlot;
	
	private Long customerId;
	
	private Long customerBusinessId;
	
	private Long siteId;

}
