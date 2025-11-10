package com.lifetrenz.lths.appointment.service.impl;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.model.collection.ParticipantScheduleDetails;
import com.lifetrenz.lths.appointment.service.ParticipantScheduleValidationService;

@Component
public class ParticipantScheduleValidationServiceImpl implements ParticipantScheduleValidationService {

//	@Autowired
//	AppointmentSchedulerService appointmentSchedulerService;

	@Override
	public boolean scheduleValidation(ParticipantScheduleDetails participantScheduleDetails,
			List<ParticipantScheduleDetails> confList) throws ApplicationException {

		if (confList == null || confList.isEmpty()) {
			return false;
		}

		for (ParticipantScheduleDetails confElement : confList) {
			if (isScheduleOverlapping(participantScheduleDetails, confElement)) {
				return true;
			}
		}

		return false;
	}

	private boolean isScheduleOverlapping(ParticipantScheduleDetails request, ParticipantScheduleDetails conf)
			throws ApplicationException {
		if (request.getCustomScheduleDto() == null || conf.getCustomScheduleDto() == null) {
			return false;
		}

		try {
			boolean isDateOverlapping = isDateRangeOverlapping(request, conf);
			boolean isTimeOverlapping = isTimeRangeOverlapping(request, conf);
			boolean isDayOverlapping = isDayOverlapping(request, conf);

			return isDateOverlapping && isTimeOverlapping && isDayOverlapping;
		} catch (Exception e) {
			throw new RuntimeException("Error validating schedule overlap", e);
		}
	}

	private boolean isDateRangeOverlapping(ParticipantScheduleDetails request, ParticipantScheduleDetails conf) {
		Long reqFrom = request.getCustomScheduleDto().getScheduleFrom();
		Long reqTo = request.getCustomScheduleDto().getScheduleTo();
		Long confFrom = conf.getCustomScheduleDto().getScheduleFrom();
		Long confTo = conf.getCustomScheduleDto().getScheduleTo();

		return (reqFrom >= confFrom && reqFrom <= confTo) || (reqFrom <= confFrom && reqTo >= confFrom)
				|| (reqTo >= confTo && reqFrom <= confFrom) || (reqFrom <= confFrom && reqTo >= confTo);
	}

	private boolean isTimeRangeOverlapping(ParticipantScheduleDetails request, ParticipantScheduleDetails conf) {
		LocalTime reqT1 = LocalTime.parse(request.getCustomScheduleDto().getScheduleFromTime());
		LocalTime reqT2 = LocalTime.parse(request.getCustomScheduleDto().getScheduleToTime());
		LocalTime confT1 = LocalTime.parse(conf.getCustomScheduleDto().getScheduleFromTime());
		LocalTime confT2 = LocalTime.parse(conf.getCustomScheduleDto().getScheduleToTime());

		int reqFrom = reqT1.getHour() * 60 + reqT1.getMinute();
		int reqTo = reqT2.getHour() * 60 + reqT2.getMinute();
		int confFrom = confT1.getHour() * 60 + confT1.getMinute();
		int confTo = confT2.getHour() * 60 + confT2.getMinute();

		return (reqFrom >= confFrom && reqFrom <= confTo) || (reqFrom <= confFrom && reqTo >= confFrom)
				|| (reqTo >= confTo && reqFrom <= confFrom) || (reqFrom <= confFrom && reqTo >= confTo);
	}

	private boolean isDayOverlapping(ParticipantScheduleDetails request, ParticipantScheduleDetails conf) {
		for (String day : request.getCustomScheduleDto().getDays()) {
			for (String confDay : conf.getCustomScheduleDto().getDays()) {
				if (day.equals(confDay)) {
					return true;
				}
			}
		}
		return false;
	}

}
