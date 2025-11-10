package com.lifetrenz.lths.appointment.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lifetrenz.lths.appointment.common.app.ApplicationResponse;
import com.lifetrenz.lths.appointment.common.app.constant.CommonConstants;
import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.common.app.exception.FailedException;
import com.lifetrenz.lths.appointment.common.util.AppUtils;
import com.lifetrenz.lths.appointment.dto.BookAppointmentDto;
import com.lifetrenz.lths.appointment.dto.CancelExtAppointmentDTO;
import com.lifetrenz.lths.appointment.dto.DoctorSlotUtilizationDTO;
import com.lifetrenz.lths.appointment.dto.FailedEventsGetDto;
import com.lifetrenz.lths.appointment.dto.FollowupRequestDto;
import com.lifetrenz.lths.appointment.dto.LtAppointmentCancelKafkaRequest;
import com.lifetrenz.lths.appointment.dto.OldAppointmentDto;
import com.lifetrenz.lths.appointment.dto.ReconfirmAppointmentDto;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.mapper.AppointmentMapper;
import com.lifetrenz.lths.appointment.model.collection.Appointment;
import com.lifetrenz.lths.appointment.model.events.MessageEvent;
import com.lifetrenz.lths.appointment.service.AppointmentService;
import com.lifetrenz.lths.appointment.service.FollowupRequestService;
import com.lifetrenz.lths.appointment.service.MessageEventService;
import com.lifetrenz.lths.appointment.service.TokenService;

@RestController
public class AppointmentController {

	private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

	@Autowired
	AppointmentService appointmentService;

	@Autowired
	TokenService tokenService;

	@Autowired
	AppointmentMapper appointmentMapper;

	@Autowired
	MessageEventService messageEventService;

	@Autowired
	FollowupRequestService followupRequestService;

	@PostMapping("/book/appointment")
	public ApplicationResponse<OldAppointmentDto> bookAppointmentConfig(@RequestHeader HttpHeaders headers,
			@RequestBody OldAppointmentDto appointment) {
		return handleRequest(() -> {
			// Reduced logging for performance - keep essential info logging
			if (logger.isInfoEnabled()) {
				logger.info("Booking request: customerId={}, slotId={}, startDate={}", 
					appointment.getCustomerId(), appointment.getSlotId(), appointment.getAppointmentStartDate());
			}
			
			// REMOVED DUPLICATE VALIDATION - let service handle it
			// This eliminates one database round trip while preserving all validation logic
			// The service layer already performs the same validation with proper exception handling
			
			// Call service layer directly - all validation and business logic preserved
			Appointment bookResult = this.appointmentService.bookAppoinment(appointment,
					tokenService.getTokenPayload(headers), this.getClass().getSimpleName());
			
			if (logger.isInfoEnabled()) {
				logger.info("Appointment booked successfully - appointmentId: {}, customerId: {}, slotId: {}", 
					bookResult.getId(), appointment.getCustomerId(), appointment.getSlotId());
			}
			
			// PRESERVE all existing response structure and mapping
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, appointmentMapper.mapToAppointmentDto(bookResult));
		});
	}

	@GetMapping("/appt/getSlots/{doctorId}")
	public ApplicationResponse<List<OldAppointmentDto>> getAppointmentSlots(@PathVariable String doctorId) {
		return handleRequest(() -> new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
				String.valueOf(HttpStatus.CREATED), null,
				this.appointmentService.getApptSlots(doctorId, this.getClass().getSimpleName())));
	};

	@GetMapping("/patient/{patientId}")
	public ApplicationResponse<List<OldAppointmentDto>> getPatientAppointment(@PathVariable String patientId,
			@RequestParam() String startDate, @RequestParam() String endDate) throws ApplicationException {
		try {
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, this.appointmentService.getPatientAppointments(patientId,
							startDate, endDate, this.getClass().getSimpleName()));
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	};

	@GetMapping("/appt")
	public ApplicationResponse<List<OldAppointmentDto>> getAppointmentSlots(@RequestParam() Long customerBusinessId,
			@RequestParam() Long customerId, @RequestParam() Long siteId, @RequestParam() String speciality,
			@RequestParam() String startDate, @RequestParam() String endDate, @RequestParam() String participantId,
			@RequestParam() String participantType, @RequestParam() String serviceType) throws ApplicationException {
		try {
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null,
					this.appointmentService.getApptSlots(customerBusinessId, customerId, siteId, speciality,
							AppUtils.isNullString(startDate) ? null : Long.valueOf(startDate),
							AppUtils.isNullString(endDate) ? null : Long.valueOf(endDate), participantId,
							participantType, serviceType, this.getClass().getSimpleName()));
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	};

	@GetMapping("/availability")
	public ApplicationResponse<Boolean> getCareTeamAvailibilty(@RequestParam() Long customerBusinessId,
			@RequestParam() Long customerId, @RequestParam() Long siteId, @RequestParam() String speciality,
			@RequestParam() String startDate, @RequestParam() String endDate, @RequestParam() String participantId,
			@RequestParam() String participantType, @RequestParam() String serviceType) throws Exception {
		try {

			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null,
					this.appointmentService.getAvailabily(customerBusinessId, customerId, siteId,
							AppUtils.isNullString(startDate) ? null : Long.valueOf(startDate),
							AppUtils.isNullString(endDate) ? null : Long.valueOf(endDate), participantId,
							participantType, this.getClass().getSimpleName()));
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}

	};

	@PutMapping("/cancel/appt")
	public ApplicationResponse<LtAppointmentCancelKafkaRequest> cancelAppt(@RequestHeader HttpHeaders headers,
			@RequestBody CancelExtAppointmentDTO cancelAppointmentDTO) throws ApplicationException {

		try {
			TokenPayLoad tokenPayLoad = tokenService.getTokenPayload(headers);

			LtAppointmentCancelKafkaRequest appCancel = new LtAppointmentCancelKafkaRequest(
					cancelAppointmentDTO.getReferenceAppointmentId(), cancelAppointmentDTO.getExternalAppointmentId(),
					"cancelled", cancelAppointmentDTO.getCancellationReason(), cancelAppointmentDTO.getReasonCode(),
					cancelAppointmentDTO.getUserName(), null, tokenPayLoad, cancelAppointmentDTO.getIsReschedule());

			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, this.appointmentService.cancleApptnt(appCancel,
							cancelAppointmentDTO.getSource(), tokenPayLoad.getName(), this.getClass().getSimpleName()));
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@GetMapping("/nonavailability/appt")
	public ApplicationResponse<List<OldAppointmentDto>> getNonAvb(@RequestParam() Long siteId,
			@RequestParam() String startDate, @RequestParam() String endDate, @RequestParam() String doctorId,
			@RequestParam() String patient, @RequestParam() String participantType, @RequestParam() String visitType,
			@RequestParam() String appiointmentStatus, @RequestParam() String participant) throws ApplicationException {
		try {
			List<Appointment> result = this.appointmentService.getNonAvb(siteId,
					startDate.equals("") ? 0 : Long.valueOf(startDate), endDate.equals("") ? 0 : Long.valueOf(endDate),
					doctorId, patient, participantType, visitType, appiointmentStatus, participant,
					this.getClass().getSimpleName());
			List<OldAppointmentDto> response = result.stream().map(app -> appointmentMapper.mapToAppointmentDto(app))
					.collect(Collectors.toList());
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, response);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}

	}

	@GetMapping("/patient/previous/appointment")
	public ApplicationResponse<List<OldAppointmentDto>> getPatientPreviousAppointment(@RequestParam() String patientId,
			@RequestParam() Long siteId, @RequestParam() String doctorSpecialityIdentifier, String doctorId,
			Long startDate, Long endDate, String visitType, String appointmentMode) {
		try {
			List<Appointment> result = this.appointmentService.getPatientAppointment(patientId, siteId,
					doctorSpecialityIdentifier, doctorId, startDate, endDate, visitType, appointmentMode,
					this.getClass().getSimpleName());
			List<OldAppointmentDto> response = null;
			if (result != null) {
				response = result.stream().map(app -> appointmentMapper.mapToAppointmentDto(app))
						.collect(Collectors.toList());
			}
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, response);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}

	}

	@GetMapping("/mob/patient/appointment")
	public ApplicationResponse<List<BookAppointmentDto>> getMobPatientAppointment(
			@RequestParam(name = "patientId", required = true) String patientId,
			@RequestParam(name = "startDate", required = false) Long startDate,
			@RequestParam(name = "endDate", required = false) Long endDate,
			@RequestParam(name = "appStatus", required = false) String appStatus) {
		try {
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, this.appointmentService.getMobPatientAppointment(
							patientId, startDate, endDate, appStatus, this.getClass().getSimpleName()));
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}

	}

	@GetMapping("existing/patient/appointment")
	public ApplicationResponse<List<OldAppointmentDto>> getExistingPatientAppointment(@RequestParam() Long Date,
			@RequestParam() String participantId, @RequestParam() Long siteId, @RequestParam() String patientId)
			throws ApplicationException {
		try {
			List<Appointment> result = this.appointmentService.findExistingAppointment(Date, participantId, siteId,
					patientId, this.getClass().getSimpleName());
			List<OldAppointmentDto> response = result.stream().map(app -> appointmentMapper.mapToAppointmentDto(app))
					.collect(Collectors.toList());
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, response);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}

	}

	@GetMapping("/cc/appointment")
	public ApplicationResponse<List<OldAppointmentDto>> getCallcenterAppointment(
			@RequestParam() Long customerBusinessId, @RequestParam() Long customerId, @RequestParam() Long siteId,
			@RequestParam() String speciality, @RequestParam() String startDate, @RequestParam() String endDate,
			@RequestParam() String participantId, @RequestParam() String participantType,
			@RequestParam() String serviceType, @RequestParam() String payerType,
			@RequestParam() String appointmentStatus, @RequestParam() String appointmentCategory,
			@RequestParam() String number, @RequestParam() String appointmentConductMode,
			@RequestParam() String patientName, @RequestParam() String visitType, @RequestParam() String role)
			throws ApplicationException {

		try {
			List<Appointment> getAppointmentSlot = this.appointmentService.getCallcenterAppointment(customerBusinessId,
					customerId, siteId, speciality, AppUtils.isNullString(startDate) ? null : Long.valueOf(startDate),
					AppUtils.isNullString(endDate) ? null : Long.valueOf(endDate), participantId, participantType,
					serviceType, payerType, appointmentStatus, appointmentCategory, number, appointmentConductMode,
					patientName, visitType, role, this.getClass().getSimpleName());
			List<OldAppointmentDto> response = getAppointmentSlot.stream()
					.map(app -> appointmentMapper.mapToAppointmentDto(app)).collect(Collectors.toList());
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, response);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	};

	@GetMapping("/rc/appointment")
	public ApplicationResponse<List<OldAppointmentDto>> getCallcenterReconfirmAppointment(
			@RequestParam() Long customerBusinessId, @RequestParam() Long customerId, @RequestParam() Long siteId,
			@RequestParam() String speciality, @RequestParam() String startDate, @RequestParam() String endDate,
			@RequestParam() String participantId, @RequestParam() String participantType,
			@RequestParam() String serviceType, @RequestParam() String payerType,
			@RequestParam() String appointmentStatus, @RequestParam() String appointmentCategory,
			@RequestParam() String number, @RequestParam() String appointmentConductMode,
			@RequestParam() String patientName, @RequestParam() String visitType, @RequestParam() String role)
			throws ApplicationException {

		try {
			List<Appointment> getAppointmentSlot = this.appointmentService.getCallcenterReconfirmAppointment(
					customerBusinessId, customerId, siteId, speciality,
					AppUtils.isNullString(startDate) ? null : Long.valueOf(startDate),
					AppUtils.isNullString(endDate) ? null : Long.valueOf(endDate), participantId, participantType,
					serviceType, payerType, appointmentStatus, appointmentCategory, number, appointmentConductMode,
					patientName, visitType, role, this.getClass().getSimpleName());
			List<OldAppointmentDto> response = getAppointmentSlot.stream()
					.map(app -> appointmentMapper.mapToAppointmentDto(app)).collect(Collectors.toList());
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, response);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	};

	@PutMapping("/reconfirm/appointment/{appointmentId}")
	public ApplicationResponse<ReconfirmAppointmentDto> reconfirmAppointment(@RequestHeader HttpHeaders headers,
			@PathVariable String appointmentId, @RequestBody ReconfirmAppointmentDto reconfirmAppoinment)
			throws ApplicationException {
		try {
			ReconfirmAppointmentDto response = this.appointmentService.reconfirmAppoinment(appointmentId,
					reconfirmAppoinment, this.getClass().getSimpleName());
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, response);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@GetMapping("/failed/events/data")
	public ApplicationResponse<List<FailedEventsGetDto>> getFailedEvents(
			@RequestParam(defaultValue = "") Long startDate, @RequestParam(defaultValue = "") Long endDate,
			@RequestParam(defaultValue = "") String serviceName, @RequestParam(defaultValue = "") String topicName)
			throws ApplicationException, Exception {

		List<FailedEventsGetDto> result = null;
		List<MessageEvent> response = null;
		try {
			response = this.messageEventService.getAllFailedEvents(startDate, endDate, serviceName, topicName);
		} catch (Exception e) {
			throw new FailedException("Failed to get Message Event !");
		}

		try {
			result = response.stream().map(msg -> appointmentMapper.mapToMessageEventDto(msg))
					.collect(Collectors.toList());
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, result);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	};

	@GetMapping(value = "/get/appointment/data/for/slots")
	public ApplicationResponse<List<DoctorSlotUtilizationDTO>> getDocSlot(
			@RequestParam(name = "createdOn", required = true) Long createdOn) throws ApplicationException, Exception {
		List<DoctorSlotUtilizationDTO> response = null;
		try {
			response = this.appointmentService.getDoctorSlotUtilization(createdOn, this.getClass().getSimpleName());
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, response);

		} catch (ApplicationException e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@PutMapping(value = "opd/repush/events")
	public ApplicationResponse<Boolean> repushEvents(@RequestHeader HttpHeaders httpHeaders,
			@RequestParam(defaultValue = "eventId", required = true) String eventId) throws ApplicationException {
		try {
			TokenPayLoad tokenPayload = tokenService.getTokenPayload(httpHeaders);
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null,
					this.appointmentService.repushEvents(eventId, tokenPayload, this.getClass().getSimpleName()));
		} catch (Exception e) {
			// TODO: handle exception
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}

	}

	@PostMapping(value = "followup/request")
	public ApplicationResponse<FollowupRequestDto> addFollowupRequest(@RequestHeader HttpHeaders headers,
			@RequestBody FollowupRequestDto followup) throws ApplicationException {

		try {

			FollowupRequestDto bookResult = this.followupRequestService.creatFollowupRequest(followup);
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, bookResult);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@GetMapping(value = "/get/followup/request")
	public ApplicationResponse<List<FollowupRequestDto>> getFollowupRequest() throws ApplicationException, Exception {
		List<FollowupRequestDto> response = null;
		try {
			response = this.followupRequestService.getAllFollowupRequest();
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, response);
		} catch (ApplicationException e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@PutMapping(value = "update/followup/request")
	public ApplicationResponse<FollowupRequestDto> updateFollowupRequest(@RequestHeader HttpHeaders headers,
			@RequestBody FollowupRequestDto followup) throws ApplicationException {

		try {
			TokenPayLoad tokenPayload = tokenService.getTokenPayload(headers);
			FollowupRequestDto bookResult = this.followupRequestService.updateFollowupRequest(followup, tokenPayload);
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, bookResult);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@GetMapping(value = "/search/followup/request")
	public ApplicationResponse<List<FollowupRequestDto>> searchFolloupRequest(
			@RequestParam(defaultValue = "") String userName, @RequestParam(defaultValue = "") Long referredBy,
			@RequestParam(defaultValue = "") Long referredTo, @RequestParam(defaultValue = "") String patientNameMpi,
			@RequestParam(defaultValue = "") Long fromDate, @RequestParam(defaultValue = "") Long toDate,
			@RequestParam(defaultValue = "") String status, @RequestParam(defaultValue = "") String eventIdentifier,
			@RequestParam(required = false) Long referredSite, @RequestParam(defaultValue = "") String specialization,
			@RequestParam(defaultValue = "") String priority, @RequestParam(defaultValue = "") String site,
			@RequestParam(defaultValue = "") String referralType)
			throws ApplicationException, Exception {
		List<FollowupRequestDto> response = null;
		try {
			response = this.followupRequestService.searchFollowupRequests(userName, referredBy, referredTo,
					patientNameMpi, fromDate, toDate, status, eventIdentifier, referredSite, specialization, priority,
					site, referralType);
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, response);

		} catch (ApplicationException e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@GetMapping("check/participant/appointment")
	public ApplicationResponse<List<OldAppointmentDto>> checkAppointmentForDateTime(@RequestParam() Long date,
			@RequestParam() String participantId, @RequestParam() Long siteId, @RequestParam String appointmentCategory)
			throws ApplicationException {
		try {
			List<Appointment> result = this.appointmentService.checkAppointmentForDateTime(date, participantId, siteId,
					appointmentCategory, this.getClass().getSimpleName());
			List<OldAppointmentDto> response = result.stream().map(app -> appointmentMapper.mapToAppointmentDto(app))
					.collect(Collectors.toList());
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, response);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}

	}

	private <T> ApplicationResponse<T> handleRequest(CheckedSupplier<ApplicationResponse<T>> supplier) {
		try {
			return supplier.get();
		} catch (Exception e) {
			logger.error("Error occurred: ", e);
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@FunctionalInterface
	private interface CheckedSupplier<T> {
		T get() throws Exception;
	}
}