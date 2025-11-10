/**
 * 
 */
package com.lifetrenz.lths.appointment.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifetrenz.lths.appointment.common.app.ApplicationResponse;
import com.lifetrenz.lths.appointment.common.app.constant.CommonConstants;
import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.dto.BlockEventDto;
import com.lifetrenz.lths.appointment.dto.BlockSchedularDto;
import com.lifetrenz.lths.appointment.dto.BlockScheduleStatusDto;
import com.lifetrenz.lths.appointment.dto.ScheduleEventResponseDto;
import com.lifetrenz.lths.appointment.dto.ScheduleHolidayDTO;
import com.lifetrenz.lths.appointment.dto.SchedulerEventDto;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.dto.UpdateScheduleEventDto;
import com.lifetrenz.lths.appointment.model.collection.SchedulerEvent;
import com.lifetrenz.lths.appointment.model.collection.SchedulerEventData;
import com.lifetrenz.lths.appointment.service.ScheduleHolidayService;
import com.lifetrenz.lths.appointment.service.SchedulerEventService;
import com.lifetrenz.lths.appointment.service.TokenService;

/**
 * @author Ajith.K
 *
 */
@RestController
public class SchedulerEventController {

	@Autowired
	SchedulerEventService schedulerEventService;

	@Autowired
	TokenService tokenService;

	@Autowired
	ScheduleHolidayService scheduleHolidayService;

	@GetMapping("/scheduler/events")
	public ApplicationResponse<List<SchedulerEventDto>> getSchedulerEvent(@RequestParam(required = true) Long siteId,
			@RequestParam(required = true) String[] calendarType, @RequestParam(required = false) String participant,
			@RequestParam(required = true) Long scheduleFrom, @RequestParam(required = true) Long scheduleTo,
			@RequestParam() String participantType) {
		return handleRequest(() -> schedulerEventService.getScheduleEvent(siteId, calendarType, participant,
				scheduleFrom, scheduleTo, participantType, this.getClass().getSimpleName()));
	}

	@PutMapping("/scheduler/events/{id}")
	public ApplicationResponse<List<SchedulerEventDto>> updateSchedulerEvent(@RequestHeader HttpHeaders headers,
			@PathVariable("id") String id, @RequestBody SchedulerEventData schedulerEventData) {
		return handleRequest(() -> {
			TokenPayLoad tokenPayLoad = tokenService.getTokenPayload(headers);
			return schedulerEventService.updateEventData(id, schedulerEventData, tokenPayLoad,
					this.getClass().getSimpleName());
		});
	}

	@PutMapping("/scheduler/event/delete/{id}")
	public ApplicationResponse<List<SchedulerEventDto>> deleteEventById(@RequestHeader HttpHeaders headers,
			@RequestBody SchedulerEventData schedulerEventData, @PathVariable("id") String id) {
		return handleRequest(() -> {
			TokenPayLoad tokenPayLoad = tokenService.getTokenPayload(headers);
			return schedulerEventService.deleteEventById(id, schedulerEventData, tokenPayLoad,
					this.getClass().getSimpleName());
		});
	}

	@GetMapping("user/all/scheduler/events")
	public ApplicationResponse<List<SchedulerEventDto>> getUserSchedulerEvent(
			@RequestParam(required = true) Long siteId, @RequestParam(required = true) String[] calendarType,
			@RequestParam(required = true) String participant, @RequestParam(required = true) Long scheduleFrom,
			@RequestParam(required = true) Long scheduleTo, @RequestParam() String participantType) {
		return handleRequest(() -> schedulerEventService.getUserSchedulerEvent(siteId, calendarType, participant,
				scheduleFrom, scheduleTo, participantType, this.getClass().getSimpleName()));
	}

	@PostMapping("block/events")
	public ApplicationResponse<SchedulerEventDto> blockEvents(@RequestHeader HttpHeaders headers,
			@RequestBody BlockEventDto blockDto) throws Exception {
		try {
			SchedulerEventDto result = this.schedulerEventService.blockEvents(blockDto,
					this.getClass().getSimpleName());
			return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()),
					CommonConstants.OK, result);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@GetMapping("block/events")
	public ApplicationResponse<List<BlockSchedularDto>> getBlockSchedular(@RequestHeader HttpHeaders headers,
			@RequestParam(required = true) Long customerBusinessId, @RequestParam(required = true) Long siteId,
			@RequestParam(required = false) Long userId, @RequestParam(required = true) Long fromDate,
			@RequestParam(required = true) Long toDate) throws Exception {
		try {
			return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()),
					CommonConstants.OK, this.schedulerEventService.getBlockSchedular(customerBusinessId, siteId, userId,
							fromDate, toDate, this.getClass().getSimpleName()));
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@GetMapping("block/events/history")
	public ApplicationResponse<List<BlockSchedularDto>> getBlockSchedularHistory(@RequestHeader HttpHeaders headers,
			@RequestParam(required = true) String schedulerEventId) throws Exception {
		try {
			return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()),
					CommonConstants.OK, this.schedulerEventService.getBlockSchedularHistory(schedulerEventId,
							this.getClass().getSimpleName()));
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@PutMapping("block/events/update")
	public ApplicationResponse<BlockSchedularDto> updateSlotStatus(@RequestHeader HttpHeaders headers,
			@RequestBody BlockScheduleStatusDto blockDto) throws Exception {
		try {
			return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()),
					CommonConstants.OK,
					this.schedulerEventService.updateSlotStatus(blockDto, this.getClass().getSimpleName()));
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@PostMapping(value = "/committee/schedule/meetings")
	public ApplicationResponse<List<ScheduleEventResponseDto>> saveScheuleEvent(@RequestParam String request)
			throws ApplicationException {
		try {
			List<SchedulerEvent> scheduleEventList;
			List<ScheduleEventResponseDto> responseList = new ArrayList<>();

			String[] resultRequest = request.split("~");
			if (resultRequest.length > 0) {
				scheduleEventList = Arrays
						.asList(new ObjectMapper().readValue(resultRequest[0], SchedulerEvent[].class));

				for (SchedulerEvent item : scheduleEventList) {
					item.setReferrenceId(resultRequest[1]);
					try {
						SchedulerEvent result = this.schedulerEventService.saveScheuleEvent(item, null,
								this.getClass().getSimpleName());

						if (result != null && result.getParticipant() != null) {
							ScheduleEventResponseDto dto = new ScheduleEventResponseDto(result.getId(),
									result.getParticipant().getParticipantId(), result.getReferrenceId());

							responseList.add(dto);
						}
					} catch (ApplicationException e) {
						// Log the exception
					}
				}
			}

			return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()),
					CommonConstants.OK, responseList);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@PostMapping(value = "schedule/holiday/date/post")
	public ApplicationResponse<ScheduleHolidayDTO> saveHolidayDate(@RequestHeader HttpHeaders headers,
			@RequestBody ScheduleHolidayDTO schduleHoildayDTO) throws Exception {
		ScheduleHolidayDTO result = null;
		TokenPayLoad tokenPayLoad = null;
		try {
			tokenPayLoad = tokenService.getTokenPayload(headers);
			result = this.scheduleHolidayService.saveHoildayDate(schduleHoildayDTO, tokenPayLoad,
					this.getClass().getSimpleName());
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.OK.value()), null, result);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}

	}

	@DeleteMapping(value = "delete/schedule/holiday/date/{id}")
	public ApplicationResponse<?> deleteSchedulHoliday(@RequestHeader HttpHeaders headers, @PathVariable String id)
			throws ApplicationException {
		TokenPayLoad tokenPayLoad = null;
		try {
			tokenPayLoad = tokenService.getTokenPayload(headers);

			Boolean result = this.scheduleHolidayService.deleteSchedulHoliday(tokenPayLoad, id);
			return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()),
					CommonConstants.OK, result);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@GetMapping(value = "get/all/schedule/holiday/date")
	public ApplicationResponse<List<ScheduleHolidayDTO>> getAllScheduleHoilday(
			@RequestParam(name = "hoildayName", required = false) String hoildayName,
			@RequestParam(name = "holidayDate", required = false) Date holidayDate,
			@RequestParam(name = "descripation", required = false) String descripation) throws ApplicationException {
		List<ScheduleHolidayDTO> response = null;
		try {
			response = this.scheduleHolidayService.getAllScheduleHoilday();
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS),
					String.valueOf(HttpStatus.CREATED), null, response);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@GetMapping("get/schedule/holiday/date")
	public ApplicationResponse<ScheduleHolidayDTO> getScheduleHoilday(
			@RequestParam(name = "holidayName", required = true) String holidayName,
			@RequestParam(name = "holidayDate", required = false) Date holidayDate,
			@RequestParam(name = "description", required = false) String description) throws ApplicationException {

		ScheduleHolidayDTO response = null;
		try {
			response = this.scheduleHolidayService.getScheduleHoilday(holidayName);
			return new ApplicationResponse<>(String.valueOf(CommonConstants.SUCCESS), String.valueOf(HttpStatus.OK),
					null, response);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@PostMapping("ot/schedule/update/event")
	public ApplicationResponse<?> updateScheduleEventStatus(@RequestHeader HttpHeaders headers,
			@RequestBody UpdateScheduleEventDto updateScheduleEventDto) throws Exception {
		try {
			UpdateScheduleEventDto result = this.schedulerEventService.updateScheduleEventStatus(updateScheduleEventDto,
					this.getClass().getSimpleName());
			return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()),
					CommonConstants.OK, result);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@GetMapping("/scheduler/next-available-slot")
	public ApplicationResponse<SchedulerEventDto> getNextAvailableSlot(
			@RequestParam("participantId") String participantId, @RequestParam("fromDate") Long fromDate,
			@RequestParam("toDate") Long toDate) {
		return handleRequest(
				() -> schedulerEventService.getNextAvailableSlot(participantId, new Date(fromDate), new Date(toDate)));
	}

	private <T> ApplicationResponse<T> handleRequest(RequestHandler<T> handler) {
		try {
			T result = handler.handle();
			return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()),
					CommonConstants.OK, result);
		} catch (ApplicationException e) {
			return new ApplicationResponse<>(CommonConstants.ERROR, String.valueOf(e.getCode()), e.getMessage(), null);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), "An unexpected error occurred", null);
		}
	}

	@FunctionalInterface
	private interface RequestHandler<T> {
		T handle() throws Exception;
	}

	@PutMapping(value = "update/schedule/holiday/date/{id}")
	public ApplicationResponse<?> updateSchedulHoliday(@RequestHeader HttpHeaders headers, @PathVariable String id,
			@RequestBody ScheduleHolidayDTO schduleHoildayDTO) throws ApplicationException {
		TokenPayLoad tokenPayLoad = null;
		try {
			tokenPayLoad = tokenService.getTokenPayload(headers);

			Boolean result = this.scheduleHolidayService.updateSchedulHoliday(tokenPayLoad, schduleHoildayDTO);
			return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()),
					CommonConstants.OK, result);
		} catch (Exception e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

}
