package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceAppointmentDetailsDTO implements Serializable {

	private static final long serialVersionUID = 576365136629263503L;

	private Long appointmentId;

	private String referenceAppointmentId;

	private SystemMasterDTO appointmentStatus;

	private SystemMasterDTO appointmentMode;

	private Boolean waitingList;

	private List<ServiceParticipantDetails> participants;
}
