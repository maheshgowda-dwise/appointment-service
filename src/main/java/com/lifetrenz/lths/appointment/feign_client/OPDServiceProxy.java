/**
 * 
 */
package com.lifetrenz.lths.appointment.feign_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.lifetrenz.lths.appointment.dto.AppointmentResponse;
import com.lifetrenz.lths.appointment.dto.CancelExtAppointmentDTO;
import com.lifetrenz.lths.appointment.dto.LTCSBookAppointment;
import com.lifetrenz.lths.appointment.dto.NonAvabRequestDTO;
import com.lifetrenz.lths.appointment.dto.UpdateClinicalScheduleDTO;

/**
 *
 */
@FeignClient(name = "ipd")
public interface OPDServiceProxy {

	@PostMapping(value = "/book/appointment")
	public ResponseEntity<LTCSBookAppointment> createAppointment(
            @RequestBody LTCSBookAppointment csBookAppointmentDTO);

	@PutMapping(value = "core/cancel/appointment")
	public ResponseEntity<CancelExtAppointmentDTO> cancelAppointment(
			@RequestHeader(value = "access-token", required = true) String authorizationHeader,
			CancelExtAppointmentDTO csBookAppointmentDTO);

	@GetMapping(value = "/all/appointment/doctor")
	public ResponseEntity<AppointmentResponse> getAllAppointments(@RequestParam(name = "doctorId") String doctorId,
			@RequestParam() String startDate, @RequestParam() String endDate);

	@PutMapping(value = "/get/nonavb/appt")
	public ResponseEntity<?> getNonAvb(
			@RequestHeader(value = "access-token", required = true) String authorizationHeader,
			@RequestBody NonAvabRequestDTO nonAvabRequestDTO);

	@PutMapping("/clinical/schedule")
	public ResponseEntity<?> updateSchedule(
			@RequestHeader(value = "access-token", required = true) String authorizationHeader,
			@RequestBody UpdateClinicalScheduleDTO updateClinicalScheduleDTO) throws Exception;

}
