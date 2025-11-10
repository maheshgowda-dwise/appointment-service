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
public class KafkaTumorBoardDTO {

	private String id;
	private String boardRoom;
	private Date selectedDate;
	private String tumborBoardScheduleId;
	private Long coreAppointmentId;
	private Long corePatientId;
	private TokenPayLoad tokenPayload;
	private Long siteId;
}
