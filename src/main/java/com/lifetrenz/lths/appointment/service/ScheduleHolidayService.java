package com.lifetrenz.lths.appointment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.dto.ScheduleHolidayDTO;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;

@Service
public interface ScheduleHolidayService {

	public ScheduleHolidayDTO saveHoildayDate(ScheduleHolidayDTO schduleHoildayDTO, TokenPayLoad tokenPayLoad,
			String path) throws Exception;

	public Boolean deleteSchedulHoliday(TokenPayLoad tokenPayLoad, String id) throws ApplicationException;

	public List<ScheduleHolidayDTO> getAllScheduleHoilday() throws ApplicationException;

	public ScheduleHolidayDTO getScheduleHoilday(String holidayName) throws ApplicationException;

	public Boolean updateSchedulHoliday(TokenPayLoad tokenPayLoad, ScheduleHolidayDTO schduleHoildayDTO)
			throws Exception;

}
