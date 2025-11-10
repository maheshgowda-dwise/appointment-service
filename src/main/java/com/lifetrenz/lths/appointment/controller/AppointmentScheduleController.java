package com.lifetrenz.lths.appointment.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lifetrenz.lths.appointment.common.app.ApplicationResponse;
import com.lifetrenz.lths.appointment.common.app.constant.CommonConstants;
import com.lifetrenz.lths.appointment.common.app.constant.RestConstant;
import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
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
import com.lifetrenz.lths.appointment.mapper.ScheduleMapper;
import com.lifetrenz.lths.appointment.model.collection.ParticipantScheduleDetails;
import com.lifetrenz.lths.appointment.service.AppointmentSchedulerService;
import com.lifetrenz.lths.appointment.service.TokenService;

@RestController
public class AppointmentScheduleController {

	@Autowired
	AppointmentSchedulerService appointmentSchedulerService;

	@Autowired
	TokenService tokenService;
	
	@Autowired
	ScheduleMapper scheduleMapper;

	@PostMapping("participant/schedule")
	public ApplicationResponse<ParticipantScheduleResponseDto> saveConfiguration(@RequestHeader HttpHeaders headers,
			@RequestBody ParticipantScheduleDetails appointmentSchedular) {
		try {
			TokenPayLoad tokenPayLoad = tokenService.getTokenPayload(headers);
			ParticipantScheduleResponseDto response = this.appointmentSchedulerService.saveAppointmentSchedular(
					appointmentSchedular, tokenPayLoad, this.getClass().getSimpleName());
			return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()),
					CommonConstants.OK, response);
		} catch (ApplicationException e) {
			return new ApplicationResponse<>(CommonConstants.ERROR, String.valueOf(HttpStatus.BAD_REQUEST.value()),
					e.getMessage(), null);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
					"An unexpected error occurred while saving configuration.", null);
		}

	}

	@GetMapping("participant/schedule")
	public ApplicationResponse<List<ParticipantScheduleGetDto>> getConfiguration(@RequestHeader HttpHeaders headers,
			@RequestParam() Long siteId, @RequestParam() String calendarType, @RequestParam() String participantType,
			@RequestParam() String participantId, @RequestParam(required = false) List<String> scheduleType) {
		try {
			TokenPayLoad tokenPayLoad = tokenService.getTokenPayload(headers);
			List<String> scheduleTypeList = Optional.ofNullable(scheduleType).filter(list -> !list.isEmpty()).orElse(null);
			List<ParticipantScheduleDetails> result = this.appointmentSchedulerService.getConfiguration(siteId,
					calendarType, participantType, participantId, scheduleTypeList, tokenPayLoad.getCustomerId(),
					this.getClass().getSimpleName());

			List<ParticipantScheduleGetDto> response = result.stream()
					.map(scheduleMapper::convertToParticipantScheduledGetDto).collect(Collectors.toList());

			return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()), null,
					response);
		} catch (ApplicationException e) {
			return new ApplicationResponse<>(CommonConstants.ERROR, String.valueOf(HttpStatus.BAD_REQUEST.value()),
					e.getMessage(), null);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
					"An unexpected error occurred while fetching configuration.", null);
		}

	}

	@DeleteMapping("participant/schedule/{id}/{isOverride}")
	public ApplicationResponse<ParticipantScheduleGetDto> deleteSchedule(@RequestHeader HttpHeaders headers,
			@PathVariable("id") String id, @PathVariable("isOverride") Boolean isOverride) {
		try {
			TokenPayLoad tokenPayload = this.tokenService.getTokenPayload(headers);
			ParticipantScheduleGetDto response = this.appointmentSchedulerService.deleteParticipantSchedule(id,
					tokenPayload, this.getClass().getSimpleName(), isOverride);
			return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()), null,
					response);
		} catch (ApplicationException e) {
			return new ApplicationResponse<>(CommonConstants.ERROR, String.valueOf(HttpStatus.BAD_REQUEST.value()),
					e.getMessage(), null);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
					"An unexpected error occurred while deleting schedule.", null);
		}

	}

	@GetMapping("scheduled/participant")
	public ApplicationResponse<List<ScheduledParticipantGetDTO>> getScheduledParticipant(@RequestHeader HttpHeaders headers,
			@RequestParam(name = "siteId") Long siteId, @RequestParam(name = "conductingSiteId") Long conductingSiteId,
			@RequestParam() String participantType, @RequestParam(required = false) Long participantId,
			@RequestParam() String specialty, @RequestParam() String gender, @RequestParam() String nationality,
			@RequestParam() String qualification, @RequestParam() String[] language,
			@RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate,
			@RequestParam(required = false) String physicianFirstName,
			@RequestParam(required = false) String physicianMiddleName,
			@RequestParam(required = false) String physicianLastName, @RequestParam(required = false) Long isTop,
			@RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "departmentId", required = false) Long  departmentId
			) throws ApplicationException {

		TokenPayLoad tokenPayLoad = null;
		try {
			tokenPayLoad = tokenService.getTokenPayload(headers);
			List<ScheduledParticipantGetDTO> response = this.appointmentSchedulerService.getScheduledParticipant(siteId,
					conductingSiteId, participantType, participantId, specialty, gender, nationality, qualification,
					language, fromDate, toDate, physicianFirstName, physicianMiddleName, physicianLastName, isTop, type,
					tokenPayLoad.getCustomerId(), this.getClass().getSimpleName(), departmentId);
			return new ApplicationResponse<List<ScheduledParticipantGetDTO>>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.OK), null, response);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
			          String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@GetMapping("online/doctors")
	public ApplicationResponse<List<OnlineDoctors>> getAllDoctors(@RequestHeader HttpHeaders headers) throws ApplicationException {

		TokenPayLoad tokenPayLoad = null;

		try {

			tokenPayLoad = tokenService.getTokenPayload(headers);
			return new ApplicationResponse<List<OnlineDoctors>>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.OK), null, appointmentSchedulerService.getAllDoctors(tokenPayLoad.getCustomerBusinessId(), this.getClass().getSimpleName()));
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
			          String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}

	}

	@GetMapping("scheduled/participants")
	public ApplicationResponse<List<ScheduledParticipantGetDTO>> getParticipantsByCalendarType(@RequestHeader HttpHeaders headers,
			@RequestParam() String calendarType, @RequestParam() String participantType, @RequestParam() Long siteId,
			@RequestParam() Long customerBusinessId) throws ApplicationException {

		List<ScheduledParticipantGetDTO> response = null;

		try {
			TokenPayLoad tokenPayLoad = null;
			tokenPayLoad = tokenService.getTokenPayload(headers);
			response = appointmentSchedulerService.getParticipantsByCalendarType(
					calendarType.isBlank() ? null : calendarType, participantType.isBlank() ? null : participantType,
					siteId, customerBusinessId, tokenPayLoad.getCustomerId(), this.getClass().getSimpleName());
			return new ApplicationResponse<List<ScheduledParticipantGetDTO>>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.OK), null, response);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
			          String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);

		}

	}
	
	@GetMapping("doctor/slots")
	public ResponseEntity<?> getDoctorSlotes(@RequestParam() Long doctorId, @RequestParam() Long date,
			@RequestParam() Long siteId) throws Exception {

		DoctorSlots doctorSlots = null;

		try {
			doctorSlots = appointmentSchedulerService.getDoctorSlot(doctorId, date, siteId, this.getClass().getSimpleName());
		} catch (Exception e) {
			throw new Exception("No slots found!!");
		}

		return new ResponseEntity<DoctorSlots>(doctorSlots, HttpStatus.OK);

	}
	
	//Below API is for dashboard
	@GetMapping("all/slots")
    public ApplicationResponse<List<DoctorSlots>> getAllSlots(@RequestParam() Long date) throws Exception {
        List<DoctorSlots> response = null;
        try {
        	response = appointmentSchedulerService.getAllSlotsForDate(date, this.getClass().getSimpleName());
            return new ApplicationResponse<List<DoctorSlots>>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.OK), null, response);
        } catch (Exception e) {
        	return new ApplicationResponse<>(CommonConstants.ERROR,
			          String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
        }
    }

	@GetMapping("doctors")
	public ApplicationResponse<List<DoctorListDto>> getDoctorForAppointment(
			@RequestParam(name = "fromDate", required = true) Long fromDate,
			@RequestParam(name = "siteId", required = true) Long siteId) throws Exception {

		List<DoctorListDto> response = null;

		try {
			response = appointmentSchedulerService.getDoctorsForAppointmet(fromDate, siteId, this.getClass().getSimpleName());
			return new ApplicationResponse<List<DoctorListDto>>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.OK), null, response);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
			          String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}

	}

	@GetMapping("participant/schedule/list")
	public ApplicationResponse<PagenationParticipantScheduleDetailsDto> getConfigurationList(@RequestHeader HttpHeaders headers,
			@RequestParam() Long siteId, @RequestParam() String calendarType, @RequestParam() String participantType,
			@RequestParam() String participantId, @RequestParam(required = false, name = "startDate") Long startDate,
			@RequestParam(required = false, name = "endDate") Long endDate, @RequestParam(required = false) String scheduleType,
			@RequestParam(required = false, name = "speciality") String speciality,
			@RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "id") String sortBy) throws ApplicationException {

		try {
			TokenPayLoad tokenPayLoad = tokenService.getTokenPayload(headers);

			PagenationParticipantScheduleDetailsDto response = this.appointmentSchedulerService.getConfigurationList(tokenPayLoad,
					siteId, calendarType, participantType, participantId, startDate, endDate, scheduleType, speciality, this.getClass().getSimpleName()
					,pageSize, pageNo,sortBy);
			return new ApplicationResponse<PagenationParticipantScheduleDetailsDto>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.OK), null, response);

		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
			          String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}

	}

	@GetMapping(value = RestConstant.PARTICIPANT_SPECIALTIES)
	public ApplicationResponse<List<SystemMasterDTO>> getParticipantSpecialities(@RequestParam Long customerBusinessId)
			throws ApplicationException {
		List<SystemMasterDTO> result = null;
		try {
			result = this.appointmentSchedulerService.getParticipantSpecialities(customerBusinessId, this.getClass().getSimpleName());
			return new ApplicationResponse<List<SystemMasterDTO>>(CommonConstants.OK, String.valueOf(HttpStatus.OK),
					CommonConstants.SUCCESS, result);
		} catch (Exception e) {
			return new ApplicationResponse<List<SystemMasterDTO>>(CommonConstants.FAILED,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR), CommonConstants.FAILED, null);
		}

	}

	@GetMapping("recent/participant/list")
	public ApplicationResponse<List<ScheduledParticipantGetDTO>> getRecentParticipantList(@RequestHeader HttpHeaders headers, @RequestParam() Long siteId,
			@RequestParam String physicianName, @RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "departmentId", required = false) Long departmentId)throws ApplicationException{

		TokenPayLoad tokenPayLoad = null;
		try {
			tokenPayLoad = tokenService.getTokenPayload(headers);

		List<ScheduledParticipantGetDTO> response = this.appointmentSchedulerService.getRecentParticipantList(siteId,
				physicianName, type, tokenPayLoad.getCustomerId(), this.getClass().getSimpleName(), departmentId);
		
		return new ApplicationResponse<List<ScheduledParticipantGetDTO>>(String.valueOf(CommonConstants.SUCCESS), String.valueOf(HttpStatus.CREATED),
		        null, response);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
			          String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@GetMapping("my/physician/list")
	public ApplicationResponse<List<ScheduledParticipantGetDTO>> getMyPhysicianList(@RequestParam() Long siteId,
			@RequestParam String patientId)throws ApplicationException {
		try {
			List<ScheduledParticipantGetDTO> response = this.appointmentSchedulerService.getMyPhysicianList(siteId, patientId, this.getClass().getSimpleName());
			return new ApplicationResponse<List<ScheduledParticipantGetDTO>>(String.valueOf(CommonConstants.SUCCESS), String.valueOf(HttpStatus.CREATED),
			        null, response);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
			          String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}

	}

	@GetMapping(value = "participant/schedule/data")
	public ApplicationResponse<ParticipantScheduleDetailsGetDTO> getSchedularDate(@RequestHeader HttpHeaders headers,
			@RequestParam(name = "siteId") Long siteId, @RequestParam(name = "calendarType") String calendarType,
			@RequestParam() String participantType, @RequestParam(name = "participantId") String participantId,
			@RequestParam(required = false) String scheduleType, @RequestParam(required = false) Long startDate,
			@RequestParam(required = false) Long endDate, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) throws ApplicationException {
		try {
			TokenPayLoad tokenPayLoad = null;
			tokenPayLoad = tokenService.getTokenPayload(headers);
			ParticipantScheduleDetailsGetDTO response = this.appointmentSchedulerService.getParticipantDate(siteId,
					calendarType, participantType, participantId, scheduleType, startDate, endDate,
					tokenPayLoad.getCustomerId(), page, size, this.getClass().getSimpleName());
			return new ApplicationResponse<ParticipantScheduleDetailsGetDTO>(String.valueOf(CommonConstants.SUCCESS), String.valueOf(HttpStatus.CREATED),
			        null, response);

		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
			          String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}

	}

	@GetMapping("scheduled/participant/participateId")
	public ApplicationResponse<List<ScheduledParticipantGetDTO>> getPhysicianParticipateId(@RequestParam() Long siteId,
			@RequestParam Long participantId)throws ApplicationException {
		try {
			List<ScheduledParticipantGetDTO> response = this.appointmentSchedulerService.getPhysicianParticipateId(siteId,
					participantId, this.getClass().getSimpleName());
			return new ApplicationResponse<List<ScheduledParticipantGetDTO>>(String.valueOf(CommonConstants.SUCCESS), String.valueOf(HttpStatus.CREATED),
			        null, response);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
			          String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}
	
	@GetMapping("participant/speciality/schedule")
	public ApplicationResponse<List<ParticipantScheduleGetDto>> getSpecialityConfiguration(@RequestHeader HttpHeaders headers, @RequestParam() Long siteId,
			@RequestParam() String calendarType, @RequestParam() String participantType, @RequestParam() String participantId,
			@RequestParam(required = false) List<String> scheduleType) throws ApplicationException {

		try {
			TokenPayLoad tokenPayLoad = tokenService.getTokenPayload(headers);
			List<String> scheduleTypeList = Optional.ofNullable(scheduleType)
	                .filter(list -> !list.isEmpty())
	                .orElse(null);
			List<ParticipantScheduleDetails> result = this.appointmentSchedulerService.getSpecialityConfiguration(siteId,
					calendarType, participantType, participantId, scheduleTypeList, tokenPayLoad.getCustomerId(), this.getClass().getSimpleName());
			
			List<ParticipantScheduleGetDto> response = result.stream()
	                .map(scheduleMapper::convertToParticipantScheduledGetDto)
	                .collect(Collectors.toList());
			
			return new ApplicationResponse<List<ParticipantScheduleGetDto>>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.OK), null, response);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
			          String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}

	}

}
