package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleHolidayDTO {

	private String id;
	private String description;
	private String holidayName;
	private Date holidayDate;
	private Long holidaySiteId;
	private TokenPayLoad tokenPayLoad;
	private Long siteId;
	private CustomerTransactionAttributeDTO customerTrasaction;
}
