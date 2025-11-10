package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceParticipantDetails implements Serializable {

	private static final long serialVersionUID = 496472612313972554L;

	private String participantId;

	private String participantName;

	private SystemMasterDTO appointmentParticipantType;

}
