package com.lifetrenz.lths.appointment.model.collection;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.dto.ConfigBreakDto;
import com.lifetrenz.lths.appointment.dto.CustomScheduleDto;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;
import com.lifetrenz.lths.appointment.model.value_object.SystemMasterNew;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document(collection = "participant_schedule_details")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantScheduleDetails extends CustomerTransactionBase {

	private static final long serialVersionUID = 1L;

	@Id
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

	@DBRef
	private ScheduledParticipant scheduledParticipant;

	private String scheduleType;

	private String reason;

	private Long maxWaitingPerSlot;

	private Long consumedWaitingPerSession;

	private Long consumedWaitingPerSlot;

	private Boolean blockWaitingSession;

	private Boolean blockWaitingSlot;

	private String slotCategory;

	private SystemMasterNew visitType;

	private String conductingSiteName;
	private SystemMasterNew speciality;

}
