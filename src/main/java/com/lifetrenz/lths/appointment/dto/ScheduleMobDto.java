package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleMobDto implements Serializable{

	private static final long serialVersionUID = 7326734348420006814L;
	
	private Long scheduleFrom;

	private Long scheduleTo;

	private String scheduleFromTime;

	private String scheduleToTime;
	
	private String[] days;
	
	private String recurrenceRule;

}
