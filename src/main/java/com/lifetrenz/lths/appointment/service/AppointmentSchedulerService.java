package com.lifetrenz.lths.appointment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.common.app.exception.NotFoundException;
import com.lifetrenz.lths.appointment.dto.AuditEventDto;
import com.lifetrenz.lths.appointment.dto.DoctorListDto;
import com.lifetrenz.lths.appointment.dto.DoctorSlots;
import com.lifetrenz.lths.appointment.dto.OnlineDoctors;
import com.lifetrenz.lths.appointment.dto.PagenationParticipantScheduleDetailsDto;
import com.lifetrenz.lths.appointment.dto.ParticipantScheduleDetailsGetDTO;
import com.lifetrenz.lths.appointment.dto.ParticipantScheduleGetDto;
import com.lifetrenz.lths.appointment.dto.ParticipantScheduleResponseDto;
import com.lifetrenz.lths.appointment.dto.ScheduledParticipantGetDTO;
import com.lifetrenz.lths.appointment.dto.SystemMasterDTO;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.dto.UserUpdateProfileDTO;
import com.lifetrenz.lths.appointment.model.collection.ParticipantScheduleDetails;
import com.lifetrenz.lths.appointment.model.collection.ScheduledParticipant;

/**
 * 
 * @author Ajith.K
 *
 */
@Service
public interface AppointmentSchedulerService {

	/**
	 * This method used to Schedule an appointment
	 * 
	 * @param appointmentSchedular
	 * @param tokenPayload
	 * @return
	 * @throws Exception
	 */
	public ParticipantScheduleResponseDto saveAppointmentSchedular(ParticipantScheduleDetails appointmentSchedular,
			TokenPayLoad tokenPayload, String path) throws ApplicationException;

	/**
	 * To get all participant Scheduled Details
	 * 
	 * @param siteId
	 * @param calendarType
	 * @param participantType
	 * @param participantId
	 * @param scheduleType
	 * @param customerId
	 * @return
	 */
	public List<ParticipantScheduleDetails> getConfiguration(Long siteId, String calendarType, String participantType,
			String participantId, List<String> scheduleType, Long customerId, String path) throws ApplicationException;

	/**
	 * To get scheduled Scheduled Participant details
	 * 
	 * @param siteId
	 * @param conductingSiteId
	 * @param participantType
	 * @param participantId
	 * @param speciality
	 * @param gender
	 * @param nationality
	 * @param qualification
	 * @param language
	 * @param fromDate
	 * @param toDate
	 * @param physicianFirstName
	 * @param physicianMiddleName
	 * @param physicianLastName
	 * @param isTop
	 * @param type
	 * @param customerId
	 * @param departmentId
	 * @return
	 * @throws ApplicationException
	 */
	public List<ScheduledParticipantGetDTO> getScheduledParticipant(Long siteId, Long conductingSiteId,
			String participantType, Long participantId, String speciality, String gender, String nationality,
			String qualification, String[] language, String fromDate, String toDate, String physicianFirstName,
			String physicianMiddleName, String physicianLastName, Long isTop, String type, Long customerId, String path,
			Long departmentId) throws ApplicationException;

	/**
	 * This method used to delete participant schedule
	 * 
	 * @param scheduleId
	 * @param tokenPayload
	 * @return
	 * @throws ApplicationException
	 */
	public ParticipantScheduleGetDto deleteParticipantSchedule(String scheduleId, TokenPayLoad tokenPayload,
			String path, Boolean isOverride) throws ApplicationException;

	/**
	 * This method used to get All the Doctor Details
	 * 
	 * @param customerBusinessId
	 * @return
	 * @throws Exception
	 */
	public List<OnlineDoctors> getAllDoctors(Long customerBusinessId, String path)
			throws ApplicationException, Exception;

	/**
	 * To get doctor details by calendar type
	 * 
	 * @param calendarType
	 * @param participantType
	 * @param siteId
	 * @param customerBusinessId
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	public List<ScheduledParticipantGetDTO> getParticipantsByCalendarType(String calendarType, String participantType,
			Long siteId, Long customerBusinessId, Long customerId, String path) throws ApplicationException;

	/**
	 * This method used to get All Slots based on the Date value
	 * 
	 * @param date
	 * @return
	 * @throws ApplicationException
	 */
	public List<DoctorSlots> getAllSlotsForDate(Long date, String path) throws ApplicationException;

	/**
	 * 
	 * @param siteId
	 * @param calendarType
	 * @param participantType
	 * @param participantId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	PagenationParticipantScheduleDetailsDto getConfigurationList(TokenPayLoad tokenPayLoad, Long siteId,
			String calendarType, String participantType, String participantId, Long startDate, Long endDate,
			String scheduleType, String speciality, String path, Integer pageSize, Integer pageNo, String sortBy)
			throws ApplicationException;

	/**
	 * 
	 * @param userUpdateProfileDTO
	 * @return
	 * @throws ApplicationException
	 */
	ScheduledParticipant updateUserProfileStatus(UserUpdateProfileDTO userUpdateProfileDTO) throws ApplicationException;

	/**
	 * 
	 * @param coreUserId
	 * @param profilePath
	 */
	public void updateImageForUser(Long coreUserId, String profilePath);

	/**
	 * This method used to get Specialties based on scheduled Participant through
	 * CustomerId
	 * 
	 * @param customerBusinessId
	 * @return
	 * @throws NotFoundException
	 */
	public List<SystemMasterDTO> getParticipantSpecialities(Long customerBusinessId, String path)
			throws ApplicationException;

	/**
	 * 
	 * @param siteId
	 * @param participantId
	 * @throws ApplicationException
	 */
	public void updateParticipantScheduleCount(Long siteId, Long participantId) throws ApplicationException;

	/**
	 * To get recent doctor details
	 * 
	 * @param siteId
	 * @param physicianName
	 * @param type
	 * @param customerId
	 * @param departmentId
	 * @return
	 */
	public List<ScheduledParticipantGetDTO> getRecentParticipantList(Long siteId, String physicianName, String type,
			Long customerId, String path, Long departmentId) throws ApplicationException;

	/**
	 * 
	 * @param tokenPayLoad
	 * @param speciality
	 * @return
	 */
	List<ScheduledParticipant> getScheduledParticipantsBySpeciality(TokenPayLoad tokenPayLoad, String speciality,
			Integer pageSize, Integer pageNo, String sortBy);

	/**
	 * 
	 * @param siteId
	 * @param patientId
	 */
	public List<ScheduledParticipantGetDTO> getMyPhysicianList(Long siteId, String patientId, String path)
			throws ApplicationException;

	/**
	 * To get doctor list from search fields
	 * 
	 * @param siteId
	 * @param calendarType
	 * @param participantType
	 * @param participantId
	 * @param scheduleType
	 * @param startDate
	 * @param endDate
	 * @param customerId
	 * @param page
	 * @param size
	 * @return
	 */
	ParticipantScheduleDetailsGetDTO getParticipantDate(Long siteId, String calendarType, String participantType,
			String participantId, String scheduleType, Long startDate, Long endDate, Long customerId, int page,
			int size, String path) throws ApplicationException;

	/**
	 * To fetch doctor details from doctor id
	 * 
	 * @param siteId
	 * @param participantId
	 * @return
	 */
	public List<ScheduledParticipantGetDTO> getPhysicianParticipateId(Long siteId, Long participantId, String path)
			throws ApplicationException;

	/**
	 * 
	 * @param siteId
	 * @param calendarType
	 * @param participantType
	 * @param fromDate
	 * @param endDate
	 * @param scheduleType
	 * @return
	 */
	public List<ParticipantScheduleDetails> getParticipantScheduleDetails(Long siteId, String calendarType,
			String participantType, Long date, String scheduleType) throws ApplicationException;

	/**
	 * 
	 * @param date
	 * @param siteId
	 * @return
	 * @throws ApplicationException
	 */
	List<DoctorListDto> getDoctorsForAppointmet(Long date, Long siteId, String path) throws ApplicationException;

	/**
	 * 
	 * @param userName
	 * @param active
	 * @return
	 * @throws ApplicationException
	 */
	void updateUserActive(String userName, Boolean active) throws ApplicationException;

	/**
	 * 
	 * @param doctorId
	 * @param date
	 * @param siteId
	 * @return
	 * @throws ApplicationException
	 */
	DoctorSlots getDoctorSlot(Long doctorId, Long date, Long siteId, String path) throws ApplicationException;

	/**
	 * 
	 * 
	 * @param siteId
	 * @param calendarType
	 * @param participantType
	 * @param participantId
	 * @param scheduleTypeList
	 * @param customerId
	 * @param simpleName
	 * @return
	 */
	public List<ParticipantScheduleDetails> getSpecialityConfiguration(Long siteId, String calendarType,
			String participantType, String participantId, List<String> scheduleType, Long customerId, String simpleName)
			throws ApplicationException;

	/**
	 * Method used to save user login status in scheduled_participant
	 * 
	 * @param auditEvent
	 * @param isLogin
	 * @throws ApplicationException
	 */
	public void updateUserLoginStatus(AuditEventDto auditEvent, boolean isLogin) throws ApplicationException;

}
