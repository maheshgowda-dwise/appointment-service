package com.lifetrenz.lths.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Sai.KVS
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomSchedule {

	private Long scheduleFrom;

	private Long scheduleTo;

	private String scheduleFromTime;

	private String scheduleToTime;

	private String[] days;

	private String recurrenceRule;
}
