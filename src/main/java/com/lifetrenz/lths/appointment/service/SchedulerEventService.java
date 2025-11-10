/**
 * 
 */
package com.lifetrenz.lths.appointment.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.dto.BlockEventDto;
import com.lifetrenz.lths.appointment.dto.BlockSchedularDto;
import com.lifetrenz.lths.appointment.dto.BlockScheduleStatusDto;
import com.lifetrenz.lths.appointment.dto.SchedulerEventDto;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.dto.UpdateMeetingScheduledDto;
import com.lifetrenz.lths.appointment.dto.UpdateScheduleEventDto;
import com.lifetrenz.lths.appointment.model.collection.Appointment;
import com.lifetrenz.lths.appointment.model.collection.SchedulerEvent;
import com.lifetrenz.lths.appointment.model.collection.SchedulerEventData;

/**
 * @author Ajith.K
 *
 */
@Service
public interface SchedulerEventService {

	/**
	 * 
	 * @param schedulerEvent
	 * @param tokenPayload
	 * @return
	 * @throws ApplicationException
	 */
	public SchedulerEvent saveScheuleEvent(SchedulerEvent schedulerEvent, TokenPayLoad tokenPayload, String path)
			throws ApplicationException;

	/**
	 * 
	 * @param siteId
	 * @param calendarType
	 * @param participant
	 * @param scheduleFrom
	 * @param scheduleTo
	 * @return
	 * @throws ApplicationException
	 */
	public List<SchedulerEventDto> getScheduleEvent(Long siteId, String[] calendarType, String participant,
			Long scheduleFrom, Long scheduleTo, String participantType, String path) throws Exception;

	/**
	 * 
	 * @param referenceId
	 * @return
	 * @throws ApplicationException
	 */
	public List<SchedulerEvent> getScheduleEventEntityByReference(String referenceId) throws ApplicationException;

	/**
	 * 
	 * @param id
	 * @param schedulerEventData
	 * @param tokenPayload
	 * @return
	 * @throws ApplicationException
	 */
	public List<SchedulerEventDto> updateEventData(String id, SchedulerEventData schedulerEventData,
			TokenPayLoad tokenPayload, String path) throws ApplicationException;

	/**
	 * 
	 * @param id
	 * @param tokenPayload
	 * @return
	 * @throws ApplicationException
	 */
	public List<SchedulerEventDto> deleteEventById(String id, SchedulerEventData eventObj, TokenPayLoad tokenPayload, String path)
			throws ApplicationException;

	/**
	 * 
	 * @param id
	 * @param tokenPayload
	 * @return
	 * @throws ApplicationException
	 */
	public List<SchedulerEvent> deleteEventBySchedule(String id, TokenPayLoad tokenPayload) throws ApplicationException;

	/**
	 * 
	 * @param appointment
	 * @return
	 * @throws ApplicationException
	 */
	public SchedulerEvent createAppointmentEvent(Appointment appointment) throws ApplicationException;

	public SchedulerEvent updateScheuleEvent(SchedulerEvent item, int index) throws Exception;

	/**
	 * 
	 * @param appId
	 * @return
	 * @throws ApplicationException
	 */
	public List<SchedulerEvent> getScheduleEventByAppointmentId(String appId) throws ApplicationException;

	/**
	 * 
	 * @param appointmentId
	 * @return
	 * @throws ApplicationException
	 */
	public List<SchedulerEvent> deleteEventByAppointmentId(String appointmentId) throws ApplicationException;

	/**
	 * 
	 * @param eveDataId
	 * @return
	 * @throws ApplicationException
	 */
	public List<SchedulerEvent> getEventByEventDataId(Long eveDataId) throws ApplicationException;

	/**
	 * 
	 * @param schedEvent
	 * @throws ApplicationException
	 */
	public void saveSchedulerEvent(SchedulerEvent schedEvent) throws ApplicationException;

	/**
	 * 
	 * @param siteId
	 * @param calendarType
	 * @param participant
	 * @param scheduleFrom
	 * @param scheduleTo
	 * @return
	 * @throws ApplicationException
	 */
	public List<SchedulerEventDto> getUserSchedulerEvent(Long siteId, String[] calendarType, String participant,
			Long scheduleFrom, Long scheduleTo, String participantType, String path) throws Exception;

	/**
	 * 
	 * @param eventBlock
	 * @return
	 * @throws Exception
	 */
	public SchedulerEventDto blockEvents(BlockEventDto eventBlock, String path) throws ApplicationException;

	/**
	 * 
	 * @param customerBusinessId
	 * @param siteId
	 * @param userId
	 * @return
	 * @throws ApplicationException
	 */
	public List<BlockSchedularDto> getBlockSchedular(Long customerBusinessId, Long siteId, Long userId,Long fromDate,Long toDate, String path)
			throws Exception;

	/**
	 * 
	 * @param schedularId
	 * @return
	 * @throws ApplicationException
	 */
	public List<BlockSchedularDto> getBlockSchedularHistory(String schedularId, String path) throws ApplicationException;

	/**
	 * 
	 * @param status
	 * @return
	 * @throws ApplicationException
	 */
	public BlockSchedularDto updateSlotStatus(BlockScheduleStatusDto status, String path) throws ApplicationException;

	/**
	 * 
	 * @param updateMeetingScheduledDto
	 * @throws ApplicationException
	 */
	public void updateCommiteeMeetingSchedule(UpdateMeetingScheduledDto updateMeetingScheduledDto) throws ApplicationException;

	/**
	 * 
	 * 
	 * @param updateScheduleEventDto
	 * @param simpleName
	 * @return
	 * @throws Exception
	 */
	public UpdateScheduleEventDto updateScheduleEventStatus(UpdateScheduleEventDto updateScheduleEventDto,
			String simpleName) throws Exception;
	
	/**
	 * Retrieves the next available slot for a participant within a date range.
	 * 
	 * @param participantId The ID of the participant.
	 * @param fromDate The start date of the range.
	 * @param toDate The end date of the range.
	 * @return The next available slot as a SchedulerEventDto.
	 * @throws ApplicationException If an error occurs during processing.
	 */
	public SchedulerEventDto getNextAvailableSlot(String participantId, Date fromDate, Date toDate) throws ApplicationException;

}
