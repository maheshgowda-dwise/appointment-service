package com.lifetrenz.lths.appointment.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lifetrenz.lths.appointment.model.collection.CalendarScheduleType;
import com.lifetrenz.lths.appointment.model.collection.SchedulerEventData;
import com.lifetrenz.lths.appointment.model.value_object.SchedularTransactionDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchedulerEventDto {

	private String id;

	//private CustomerTransactionBase customerTransaction;

	private SchedulerEventData eventData;

	private String referrenceId;

	//private CalendarType calendarType;

	//private Participant participant;

	//private Long conductingSiteId;

	private List<SchedulerEventData> changedRecords;

	private CalendarScheduleType scheduleType;
	
	private SchedularTransactionDetails schedularTransactionDetails;

}
