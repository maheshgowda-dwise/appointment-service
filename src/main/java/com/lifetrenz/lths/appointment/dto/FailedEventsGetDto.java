package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 
 * @author Pratham.C
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailedEventsGetDto {
	
	private String id;
	
	private String topic;
	
	private Object requestBody;
	
	private String eventStatus;
	
	private String errorMessage;
	
	private Date createdOn;

	



}
