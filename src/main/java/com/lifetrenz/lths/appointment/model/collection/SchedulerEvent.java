/**
 * 
 */
package com.lifetrenz.lths.appointment.model.collection;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;
import com.lifetrenz.lths.appointment.model.value_object.SchedularTransactionDetails;
import com.lifetrenz.lths.appointment.model.value_object.appointment.CalendarType;
import com.lifetrenz.lths.appointment.model.value_object.appointment.Participant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ajith.K
 *
 */
@Document(collection = "scheduler_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchedulerEvent {

	@Id
	private String id;
	
	private CustomerTransactionBase customerTransaction;
	
	private SchedulerEventData eventData;
	
	private String referrenceId;
	
	private CalendarType calendarType;
	
	private Participant participant;
	
	@NotNull
	private Long conductingSiteId;
	
	private List<SchedulerEventData> changedRecords;
	
	private CalendarScheduleType scheduleType;
	
	private SchedularTransactionDetails schedularTransactionDetails;
}
