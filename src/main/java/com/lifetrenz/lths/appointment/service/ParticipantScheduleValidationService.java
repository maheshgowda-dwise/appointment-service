package com.lifetrenz.lths.appointment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.model.collection.ParticipantScheduleDetails;

@Service
public interface ParticipantScheduleValidationService {

	/**
	 * 
	 * @param customScheduleDto
	 * @return
	 * @throws ApplicationException
	 */

	public boolean scheduleValidation(ParticipantScheduleDetails participantScheduleDetails,
			List<ParticipantScheduleDetails> confList) throws ApplicationException;

}
