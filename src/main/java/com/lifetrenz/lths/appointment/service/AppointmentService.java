package com.lifetrenz.lths.appointment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.dto.AdmissionDashboardStatusDTO;
import com.lifetrenz.lths.appointment.dto.BookAppointmentDto;
import com.lifetrenz.lths.appointment.dto.CancelVisitDTO;
import com.lifetrenz.lths.appointment.dto.DoctorSlotUtilizationDTO;
import com.lifetrenz.lths.appointment.dto.KafkaAppointmentStatusDto;
import com.lifetrenz.lths.appointment.dto.LTAKafkaTeleconsultstionDetails;
import com.lifetrenz.lths.appointment.dto.LtAppointmentCancelKafkaRequest;
import com.lifetrenz.lths.appointment.dto.LtRescheduleAppointmentDto;
import com.lifetrenz.lths.appointment.dto.MarkArriveStatusDTO;
import com.lifetrenz.lths.appointment.dto.NeedCloseUpdateDTO;
import com.lifetrenz.lths.appointment.dto.OldAppointmentDto;
import com.lifetrenz.lths.appointment.dto.ReconfirmAppointmentDto;
import com.lifetrenz.lths.appointment.dto.RelayBookAppointmentDto;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.model.collection.Appointment;

/**
 * 
 * @author Mujaheed.N
 *
 */
@Service
public interface AppointmentService {
	/**
	 * This method used to book a appointment
	 * 
	 * @param appointment
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public Appointment bookAppoinment(OldAppointmentDto appointment, TokenPayLoad tokenPayload, String path)
			throws ApplicationException, Exception;

	/**
	 * This method used to get all the appointment, which status is not cancel
	 * 
	 * @param doctorId
	 * @param customerBusinessId
	 * @param customerId
	 * @param siteId
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public List<OldAppointmentDto> getApptSlots(Long customerBusinessId, Long customerId, Long siteId,
			String speciality, Long startDate, Long endDate, String participantId, String participantType,
			String serviceType, String path) throws ApplicationException;

//	Long getApptSlots(String doctorId) throws Exception;
	/**
	 * This method used to get all slots based on the doctor Id.
	 * 
	 * @param doctorId
	 * @return
	 * @throws Exception
	 */
	public List<OldAppointmentDto> getApptSlots(String doctorId, String path) throws ApplicationException;

	/**
	 * This method used to cancel the Appointment
	 * 
	 * @param cancelAppointmentDTO
	 * @param source
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public LtAppointmentCancelKafkaRequest cancleApptnt(LtAppointmentCancelKafkaRequest cancelAppointmentDTO,
			String source, String userName, String path) throws ApplicationException;

	/**
	 * This method used to get Appointment based on the patientId
	 * 
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public List<OldAppointmentDto> getPatientAppointments(String patientId, String startDate, String endDate,
			String path) throws ApplicationException, Exception;

	/**
	 * This method used to get Not Availablity
	 * 
	 * @param siteId
	 * @param startDate
	 * @param endDate
	 * @param doctorId
	 * @param patient
	 * @param participantType
	 * @param visitType
	 * @param appiointmentStatus
	 * @param participant
	 * @return
	 * @throws ApplicationException
	 */
	public List<Appointment> getNonAvb(Long siteId, Long startDate, Long endDate, String doctorId, String patient,
			String participantType, String visitType, String appiointmentStatus, String participant, String path)
			throws ApplicationException;

	/**
	 * This method used to get Patient Appointment
	 * 
	 * @param patientId
	 * @param siteId
	 * @param doctorSpecialityIdentifier
	 * @param doctorId
	 * @param startDate
	 * @param endDate
	 * @param visitType
	 * @param appointmentMode
	 * @return
	 */
	public List<Appointment> getPatientAppointment(String patientId, Long siteId, String doctorSpecialityIdentifier,
			String doctorId, Long startDate, Long endDate, String visitType, String appointmentMode, String path)
			throws ApplicationException;

	/**
	 * This method used for Mobility to get Appointment
	 * 
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @return To fetch appointments for patient with date range for mobile app
	 * @throws Exception
	 */
	public List<BookAppointmentDto> getMobPatientAppointment(String patientId, Long startDate, Long endDate,
			String status, String path) throws ApplicationException, Exception;

	/**
	 * 
	 * @param appointment
	 * @param username
	 * @return
	 * @throws Exception
	 */
	BookAppointmentDto bookNewAppointment(RelayBookAppointmentDto appointment) throws Exception;

//	/**
//	 * 
//	 * @param appointment
//	 * @return
//	 * @throws Exception
//	 */
//	Boolean cancelAppointment(LtAppointmentCancelKafkaRequest appointment,String source) throws Exception;

//	/**
//	 * 
//	 * @param rescheduleDto
//	 * @return
//	 * @throws Exception
//	 */
//	Appointment reshceduleAppointment(LtRescheduleAppointmentDto rescheduleDto) throws Exception;

	/**
	 * This method used to Existing Appointment
	 * 
	 * @param patientId
	 * @param Date
	 * @param participantId
	 * @param siteId
	 * @return
	 * @throws Exception
	 */

	List<Appointment> findExistingAppointment(Long Date, String participantId, Long siteId, String patientId,
			String path) throws Exception;

	/**
	 * 
	 * @param teleDetails
	 * @return
	 * @throws Exception
	 */
	Appointment setTeleconsutChennelId(LTAKafkaTeleconsultstionDetails teleDetails) throws Exception;

	/**
	 * 
	 * @param markArriveStatus
	 * @throws ApplicationException
	 */
	void updateAppointmentStatus(MarkArriveStatusDTO markArriveStatus) throws ApplicationException;

	/**
	 * 
	 * @param admissionStatus
	 * @throws ApplicationException
	 */
	void updateClinicalStatus(AdmissionDashboardStatusDTO admissionStatus) throws ApplicationException;

	/**
	 * 
	 * @param admissionStatus
	 * @throws ApplicationException
	 */
	void updateAppointmentStatus(KafkaAppointmentStatusDto admissionStatus) throws ApplicationException;

	/**
	 * This method used to get all the Call Center Data.
	 * 
	 * @param customerBusinessId
	 * @param customerId
	 * @param siteId
	 * @param speciality
	 * @param startDate
	 * @param endDate
	 * @param participantId
	 * @param participantType
	 * @param serviceType
	 * @return
	 * @throws Exception
	 */
	public List<Appointment> getCallcenterAppointment(Long customerBusinessId, Long customerId, Long siteId,
			String speciality, Long startDate, Long endDate, String participantId, String participantType,
			String serviceType, String payerType, String appoappointmentStatus, String appointmentCategory,
			String Number, String appointmentConductMode, String patientName, String visitType, String role,
			String path) throws ApplicationException, Exception;

	/**
	 * This method is used to reconfirm appointment in call center
	 * 
	 * @param customerBusinessId
	 * @param customerId
	 * @param siteId
	 * @param speciality
	 * @param startDate
	 * @param endDate
	 * @param participantId
	 * @param participantType
	 * @param serviceType
	 * @param payerType
	 * @param appoappointmentStatus
	 * @param appointmentCategory
	 * @param Number
	 * @param appointmentConductMode
	 * @param patientName
	 * @param visitType
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public List<Appointment> getCallcenterReconfirmAppointment(Long customerBusinessId, Long customerId, Long siteId,
			String speciality, Long startDate, Long endDate, String participantId, String participantType,
			String serviceType, String payerType, String appoappointmentStatus, String appointmentCategory,
			String Number, String appointmentConductMode, String patientName, String visitType, String role,
			String path) throws Exception;

	/**
	 * 
	 * @param customerBusinessId
	 * @param customerId
	 * @param siteId
	 * @param startDate
	 * @param endDate
	 * @param participantId
	 * @param participantType
	 * @return
	 * @throws Exception
	 */
	public Boolean getAvailabily(Long customerBusinessId, Long customerId, Long siteId, Long startDate, Long endDate,
			String participantId, String participantType, String path) throws Exception;

	/**
	 * This method used to Reconfirm the appointment
	 * 
	 * @param appoinmentid
	 * @param reconfirmAppointment
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public ReconfirmAppointmentDto reconfirmAppoinment(String appointmentId,
			ReconfirmAppointmentDto reconfirmAppointment, String path) throws ApplicationException;

	BookAppointmentDto hatiBookNewAppointment(RelayBookAppointmentDto appointment) throws Exception;

	public Object cancleApptntToRelay(LtAppointmentCancelKafkaRequest cancelAppointmentDTO, String source)
			throws Exception;

	public Object rescheduleApptFromRelay(LtRescheduleAppointmentDto rescheduleDTO) throws Exception;

	/**
	 * 
	 * @param needClose
	 * @throws ApplicationException
	 */
	void updateJourneyStatus(NeedCloseUpdateDTO needClose) throws ApplicationException;

	/**
	 * 
	 * @param cancelVisitDto
	 * @throws ApplicationException
	 */
	public void cancelVisitforOPpatient(CancelVisitDTO cancelVisitDto) throws ApplicationException;

	/**
	 * 
	 * @param appointment
	 * @throws ApplicationException
	 */
	public void saveAppointment(Appointment appointment) throws ApplicationException;

	/**
	 * This method used to get Doctor Slot Utilization
	 * 
	 * @param createdOn
	 * @return
	 * @throws ApplicationException
	 */
	public List<DoctorSlotUtilizationDTO> getDoctorSlotUtilization(Long createdOn, String path)
			throws ApplicationException;

	/**
	 * 
	 * @param eventId
	 * @param tokenPayload
	 * @param simpleName
	 * @return
	 * @throws ApplicationException
	 */
	public Boolean repushEvents(String eventId, TokenPayLoad tokenPayload, String path) throws ApplicationException;

	/**
	 * 
	 * @param inputDate
	 * @param participantId
	 * @param siteId
	 * @param path
	 * @return
	 * @throws Exception
	 */
	List<Appointment> checkAppointmentForDateTime(Long inputDate, String participantId, Long siteId,
			String appointmentCategory, String path) throws Exception;


	boolean isSlotAlreadyBooked(Long customerId, String slotId, Long startDate) throws ApplicationException;

}
