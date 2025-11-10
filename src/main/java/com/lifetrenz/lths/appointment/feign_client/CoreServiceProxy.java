/**
 * 
 */
package com.lifetrenz.lths.appointment.feign_client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.lifetrenz.lths.appointment.dto.AppointmentResponse;
import com.lifetrenz.lths.appointment.dto.CancelExtAppointmentDTO;
import com.lifetrenz.lths.appointment.dto.CustomerCombined;
import com.lifetrenz.lths.appointment.dto.LTCSBookAppointment;
import com.lifetrenz.lths.appointment.dto.NonAvabRequestDTO;
import com.lifetrenz.lths.appointment.dto.PatientByExternalMpiDTO;
import com.lifetrenz.lths.appointment.dto.UpdateClinicalScheduleDTO;

/**
 *
 */
@FeignClient(name = "core", url = "http://core:55003")
public interface CoreServiceProxy {

//	@GetMapping(value = "/customer/{customerId}")
//	public ResponseEntity<CustomerGroup> getCustomerById(@PathVariable("customerId") Long customerId);
//	
//	@GetMapping(value = "/customer/business/{customerBusinessId}")
//	public ResponseEntity<CustomerBusiness> getCustomerBusinessById(
//			@PathVariable("customerBusinessId") Long customerBusinessId);
//
//	@GetMapping(value = "/customer/business/{customerBusinessSiteId}")
//	public ResponseEntity<CustomerBusinessSite> getCustomerBusinessSiteById(
//			@PathVariable("customerBusinessSiteId") Long customerBusinessSiteId);

	@GetMapping(value = "customer/combined")
	public ResponseEntity<CustomerCombined> getCombined(@RequestParam("customerId") Long customerId,
			@RequestParam("customerBusinessId") Long customerBusinessId);

	@PostMapping(value = "/book/appointment")
	public ResponseEntity<LTCSBookAppointment> createAppointment(
			@RequestHeader(value = "access-token", required = true) String authorizationHeader,
			LTCSBookAppointment csBookAppointmentDTO);

	@PutMapping(value = "core/cancel/appointment")
	public ResponseEntity<CancelExtAppointmentDTO> cancelAppointment(
			@RequestHeader(value = "access-token", required = true) String authorizationHeader,
			CancelExtAppointmentDTO csBookAppointmentDTO);

//	@PutMapping(value = "/book/appointmentService/")
//	public ResponseEntity<?> bookppointment(@RequestHeader HttpHeaders headers,
//			@RequestBody Appointment appointmentServicedto);

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

	@GetMapping("patient/by/external/mpi/{externalMpi}")
	List<PatientByExternalMpiDTO> checkPatientByExternalMPI(
			@RequestHeader(value = "access-token", required = true) String username, @PathVariable String externalMpi);

}
