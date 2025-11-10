/**
 * 
 */
package com.lifetrenz.lths.appointment.model.collection;

import java.util.Date;

import com.lifetrenz.lths.appointment.model.enums.ScheduleEventStatus;
import com.lifetrenz.lths.appointment.model.enums.SlotCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ajith.K
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerEventData {

	private Long id;

	private Date startTime;

	private Date endTime;

	private String subject;

	private Boolean isAllDay;

	private String startTimezone;

	private String endTimezone;

	private String recurrenceRule;

	private Long recurrenceID;

	private String recurrenceException;

	private String resourceId;

	private String guid;

	private String followingID;

	private String parentScheduleId;

	private ScheduleEventStatus eventStatus;

	private Boolean isBlock;

	private String rankId;

	private String appointmentId;

	private String patientId;

	private String conductMode;

	private Long maximumWaitingList;

	private Long maxWaitingPerSlot;

	private Long consumedWaitingPerSession;

	private Long consumedWaitingPerSlot;

	private Boolean blockWaitingSession;

	private Boolean blockWaitingSlot;

	private String description;

	private String locationRoomName;

	private SlotCategory slotCategory;
	
	private Long waitingNumber;
private String visitTypeIdentifier;
	
	private String visitTypeName;
	private String avgMaintainceTime;


}
