package com.lifetrenz.lths.appointment.mapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.common.app.exception.FailedException;
import com.lifetrenz.lths.appointment.dto.BookAppointmentDto;
import com.lifetrenz.lths.appointment.dto.DoctorListDto;
import com.lifetrenz.lths.appointment.dto.DoctorSlotUtilizationDTO;
import com.lifetrenz.lths.appointment.dto.FailedEventsGetDto;
import com.lifetrenz.lths.appointment.dto.LTABookAppointment;
import com.lifetrenz.lths.appointment.dto.NewTelecomDTO;
import com.lifetrenz.lths.appointment.dto.OldAppointmentDto;
import com.lifetrenz.lths.appointment.dto.ParticpantCalendarDTO;
import com.lifetrenz.lths.appointment.dto.SystemMasterDTO;
import com.lifetrenz.lths.appointment.dto.TimeDto;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.model.collection.Appointment;
import com.lifetrenz.lths.appointment.model.collection.ParticipantScheduleDetails;
import com.lifetrenz.lths.appointment.model.events.MessageEvent;
import com.lifetrenz.lths.appointment.model.value_object.AppointmentParticipantType;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;
import com.lifetrenz.lths.appointment.model.value_object.DoctorGeneralDetails;
import com.lifetrenz.lths.appointment.model.value_object.DoctorRoleDetails;
import com.lifetrenz.lths.appointment.model.value_object.ParticipantDetails;
import com.lifetrenz.lths.appointment.model.value_object.ParticpantCalendar;
import com.lifetrenz.lths.appointment.model.value_object.PatientGeneralDetails;
import com.lifetrenz.lths.appointment.model.value_object.Remarks;
import com.lifetrenz.lths.appointment.util.AppUtil;

import net.fortuna.ical4j.model.DateList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.RRule;

@Component
public class AppointmentMapper {

	public Appointment mapToDocument(BookAppointmentDto request) {
		try {
			return new Appointment(request.getId(), request.getParticpantCalendar(), request.getAppointmentCategory(),
					request.getAppointmentType(), request.getAppointmentStatus(), request.getPayerType(),
					request.getVisitType(), request.getAppointmentPriority(), request.getAppointmentConductMode(),
					request.getAppointmentBookingSource(), request.getAppointmentReferralCategory(),
					request.getAppointmentBookingMode(), request.getStartDate(), request.getEndDate(),
					request.getDurationinMinutes(), request.getInstructions(), request.getAdministrativeNotes(),
					request.getReconfirmedOn(), request.getReconfirmedReason(), request.getIsWaitingList(),
					request.getExternalAppointmentId(), request.getCancelledReason(), request.getRemarks(),
					request.getIsNewVisit(), request.getAppointmentSite(), null, request.getClinicalScheduleId(),
					request.getTransactionBase(), request.getSlotId(), request.getStartDate().getTime(),
					request.getEndDate().getTime(), null, null, null, null, null, null, null, request.getIsReschedule(),
					request.getIsVip(), null, request.getVisitDetails(),null);
		} catch (Exception e) {
			throw new RuntimeException("Error mapping BookAppointmentDto to Appointment", e);
		}
	}

	public BookAppointmentDto mapToAppointmentEntity(Appointment request) {

		CustomerTransactionBase cb = new CustomerTransactionBase();
		cb.setCustomerBusinessId(request.getTransactionBase().getCustomerBusinessId());
		cb.setSiteId(request.getTransactionBase().getSiteId());
		cb.setCustomerId(request.getTransactionBase().getCustomerId());
		BookAppointmentDto res = null;
		List<ParticipantDetails> listParticipant = new ArrayList<>();

		if (request.getParticpantCalendar() != null && request.getParticpantCalendar().size() > 0) {
			for (ParticipantDetails participantDetails : request.getParticpantCalendar()) {

				ParticipantDetails details = new ParticipantDetails();
				details.setParticipantId(participantDetails.getParticipantId());
				details.setParticipantName(participantDetails.getParticipantName());
				details.setAppointmentParticipantType(participantDetails.getAppointmentParticipantType() == null ? null
						: new SystemMasterDTO(participantDetails.getAppointmentParticipantType().getId(),
								participantDetails.getAppointmentParticipantType().getNameEn(),
								participantDetails.getAppointmentParticipantType().getDescription(),
								participantDetails.getAppointmentParticipantType().getIdentifierCode()));
				if (participantDetails.getPatientDetails() != null) {
					PatientGeneralDetails patient = new PatientGeneralDetails();
					patient.setMpi(participantDetails.getPatientDetails().getMpi());
					patient.setExternalMpi(participantDetails.getPatientDetails().getExternalMpi());
					patient.setDob(participantDetails.getPatientDetails().getDob());
					patient.setGender(participantDetails.getPatientDetails().getGender());
					patient.setTelecom(participantDetails.getPatientDetails().getTelecom() == null ? null
							: new NewTelecomDTO(
									participantDetails.getPatientDetails().getTelecom().getTelecomTypeIdentifier(),
									participantDetails.getPatientDetails().getTelecom().getCountryCode(),
									participantDetails.getPatientDetails().getTelecom().getNumber()));
					details.setPatientDetails(patient);
				}
				if (participantDetails.getDoctorDetails() != null) {
					DoctorGeneralDetails doc = new DoctorGeneralDetails();

					if (participantDetails.getDoctorDetails().getRole() != null) {
						DoctorRoleDetails role = new DoctorRoleDetails();
						role.setId(participantDetails.getDoctorDetails().getRole().getId());
						role.setName(participantDetails.getDoctorDetails().getRole().getName());
						role.setIdentifier(participantDetails.getDoctorDetails().getRole().getIdentifier());
						doc.setRole(role);
					}
					details.setDoctorDetails(doc);

				}
				listParticipant.add(details);
			}
		}
		try {
			res = new BookAppointmentDto(request.getId(), listParticipant,
					request.getAppointmentCategory() == null ? null : request.getAppointmentCategory(),
					request.getAppointmentType() == null ? null : request.getAppointmentType(),
					request.getAppointmentStatus() == null ? null : request.getAppointmentStatus(),
					request.getPayerType() == null ? null
							: new SystemMasterDTO(request.getPayerType().getId(), request.getPayerType().getNameEn(),
									request.getPayerType().getDescription(),
									request.getPayerType().getIdentifierCode()),
					request.getVisitType() == null ? null
							: new SystemMasterDTO(request.getVisitType().getId(), request.getVisitType().getNameEn(),
									request.getVisitType().getDescription(),
									request.getVisitType().getIdentifierCode()),
					request.getAppointmentPriority() == null ? null : request.getAppointmentPriority(),
					request.getAppointmentConductMode() == null ? null
							: new SystemMasterDTO(request.getAppointmentConductMode().getId(),
									request.getAppointmentConductMode().getNameEn(),
									request.getAppointmentConductMode().getDescription(),
									request.getAppointmentConductMode().getIdentifierCode()),
					request.getAppointmentBookingSource() == null ? null : request.getAppointmentBookingSource(),
					request.getAppointmentReferralCategory() == null ? null : request.getAppointmentReferralCategory(),
					request.getAppointmentBookingMode() == null ? null : request.getAppointmentBookingMode(),
					request.getStartDate(), request.getEndDate(), request.getDurationinMinutes(),
					request.getInstructions(), request.getAdministrativeNotes(), null, null, request.getIsWaitingList(),
					request.getExternalAppointmentId(), "", null, true, request.getAppointmentSite(), null, cb,
					request.getSlotId() == null ? null : request.getSlotId(), request.getTeleconsultChannelId(),
					request.getEncounterStatus(), request.getAdmitStatus(), request.getJourneyStatus(),
					request.getIsReschedule(), request.getIsVip(), request.getVisitDetails());

		} catch (Exception e) {
		}
		return res;

	}

	public BookAppointmentDto mapKafkaToEntity(LTABookAppointment request) {

		List<ParticipantDetails> particpantCalendar = new ArrayList<>();
		if (request.getParticipant() != null && request.getParticipant().size() > 0) {
			for (ParticpantCalendarDTO participantDetails : request.getParticipant()) {

				ParticipantDetails details = new ParticipantDetails();
				details.setParticipantId(participantDetails.getParticipantId());
				details.setParticipantName(participantDetails.getParticipantName());
				details.setAppointmentParticipantType(
						new SystemMasterDTO(null, null, null, participantDetails.getAppointmentParticipantType()));
				if (participantDetails.getPatientDetails() != null) {
					PatientGeneralDetails patient = new PatientGeneralDetails();
					patient.setDob(participantDetails.getPatientDetails().getDob());
					patient.setGender(participantDetails.getPatientDetails().getGender());
					patient.setTelecom(participantDetails.getPatientDetails().getTelecom() == null ? null
							: new NewTelecomDTO(
									participantDetails.getPatientDetails().getTelecom().getTelecomTypeIdentifier(),
									participantDetails.getPatientDetails().getTelecom().getCountryCode(),
									participantDetails.getPatientDetails().getTelecom().getNumber()));
					details.setPatientDetails(patient);
				}
//				if (participantDetails.getRoleIdentifier() != null) {
//					DoctorGeneralDetails doc = new DoctorGeneralDetails();
//					DoctorRoleDetails role = new DoctorRoleDetails();
//					role.setId(null);
//					role.setName(null);
//					role.setIdentifier(participantDetails.getRoleIdentifier());
//					doc.setRole(role);
//					details.setDoctorDetails(doc);
//
//				}
				particpantCalendar.add(details);
			}
		}

		BookAppointmentDto res = null;
		try {
			CustomerTransactionBase cb = new CustomerTransactionBase();
			cb.setCustomerBusinessId(request.getSiteTransactionAttribute().getCustomerBusinessId());
			cb.setSiteId(request.getSiteTransactionAttribute().getSiteId());
			cb.setCustomerId(request.getSiteTransactionAttribute().getCustomerId());

			res = new BookAppointmentDto(null, particpantCalendar,
					request.getServiceCategoryIdentifier() == null ? null
							: new SystemMasterDTO(request.getServiceCategoryIdentifier().getId(),
									request.getServiceCategoryIdentifier().getNameEn(),
									request.getServiceCategoryIdentifier().getNameEn(),
									request.getServiceCategoryIdentifier().getIdentifierCode()),
					request.getServiceTypeIdentifier() == null ? null
							: new SystemMasterDTO(request.getServiceTypeIdentifier().getId(),
									request.getServiceTypeIdentifier().getNameEn(),
									request.getServiceTypeIdentifier().getNameEn(),
									request.getServiceTypeIdentifier().getIdentifierCode()),
					request.getStatusIdentifier() == null ? null
							: new SystemMasterDTO(request.getStatusIdentifier().getId(),
									request.getStatusIdentifier().getNameEn(),
									request.getStatusIdentifier().getNameEn(),
									request.getStatusIdentifier().getIdentifierCode()),
					request.getPayerTypeIdentifier() == null ? null
							: new SystemMasterDTO(request.getPayerTypeIdentifier().getId(),
									request.getPayerTypeIdentifier().getNameEn(),
									request.getPayerTypeIdentifier().getNameEn(),
									request.getPayerTypeIdentifier().getIdentifierCode()),
					request.getVisitTypeIdentifier() == null ? null
							: new SystemMasterDTO(request.getVisitTypeIdentifier().getId(),
									request.getVisitTypeIdentifier().getNameEn(),
									request.getVisitTypeIdentifier().getNameEn(),
									request.getVisitTypeIdentifier().getIdentifierCode()),
					request.getPriorityIdentifier() == null ? null
							: new SystemMasterDTO(request.getPriorityIdentifier().getId(),
									request.getPriorityIdentifier().getNameEn(),
									request.getPriorityIdentifier().getNameEn(),
									request.getPriorityIdentifier().getIdentifierCode()),
					request.getConductModeIdentifier() == null ? null
							: new SystemMasterDTO(request.getConductModeIdentifier().getId(),
									request.getConductModeIdentifier().getNameEn(),
									request.getConductModeIdentifier().getNameEn(),
									request.getConductModeIdentifier().getIdentifierCode()),
					request.getBookingSourceIdentifier() == null ? null
							: new SystemMasterDTO(request.getBookingSourceIdentifier().getId(),
									request.getBookingSourceIdentifier().getNameEn(),
									request.getBookingSourceIdentifier().getNameEn(),
									request.getBookingSourceIdentifier().getIdentifierCode()),
					request.getReferralCategoryIdentifier() == null ? null
							: new SystemMasterDTO(request.getReferralCategoryIdentifier().getId(),
									request.getReferralCategoryIdentifier().getNameEn(),
									request.getReferralCategoryIdentifier().getNameEn(),
									request.getReferralCategoryIdentifier().getIdentifierCode()),
					request.getBookingModeIdentifier() == null ? null
							: new SystemMasterDTO(request.getBookingModeIdentifier().getId(),
									request.getBookingModeIdentifier().getNameEn(),
									request.getBookingModeIdentifier().getNameEn(),
									request.getBookingModeIdentifier().getIdentifierCode()),
					new Date(request.getStartDate()), new Date(request.getEndDate()), request.getDurationinMinutes(),
					request.getInstructions(), request.getAdministrativeNotes(), null, null, request.getIswaitingList(),
					request.getExternalAppointmentId(), "", null, true, null,
					request.getScheduleId() == null ? null : String.valueOf(request.getScheduleId()), cb,
					request.getSlotId(), null, null, null, null, false, null, null);
		} catch (Exception e) {
		}

		return res;

	}

	public OldAppointmentDto mapToAppointmentDto(Appointment request) {

		ParticipantDetails docDetails = new ParticipantDetails();
		ParticipantDetails patDetails = new ParticipantDetails();
		List<ParticpantCalendar> patList = new ArrayList<>();
		if (request.getParticpantCalendar() != null && request.getParticpantCalendar().size() > 0) {
			for (ParticipantDetails participantDetails : request.getParticpantCalendar()) {
				ParticpantCalendar details = new ParticpantCalendar(participantDetails.getParticipantId(),
						participantDetails.getParticipantName(), participantDetails.getSpecialityIdentifier(),
						participantDetails.getAppointmentParticipantType() == null ? null
								: new AppointmentParticipantType(
										participantDetails.getAppointmentParticipantType().getId(),
										participantDetails.getAppointmentParticipantType().getNameEn(),
										participantDetails.getAppointmentParticipantType().getDescription(),
										participantDetails.getAppointmentParticipantType().getIdentifierCode()),
						null, null, participantDetails.getName(), participantDetails.getSalutationName(),
						participantDetails.getAliasName());
				if (participantDetails.getAppointmentParticipantType() != null) {
					if (participantDetails.getAppointmentParticipantType().getIdentifierCode().equals("Patient")) {
						patDetails = participantDetails;
					} else {
						docDetails = participantDetails;
					}
				}

				if (participantDetails.getPatientDetails() != null) {
					details.setPatientDetails(new PatientGeneralDetails(null,
							participantDetails.getPatientDetails().getMpi(),
							participantDetails.getPatientDetails().getExternalMpi(), null, null,
							participantDetails.getPatientDetails().getGender(),
							participantDetails.getPatientDetails().getDob(), null, null,
							participantDetails.getPatientDetails().getTelecom() == null ? null
									: new NewTelecomDTO(
											participantDetails.getPatientDetails().getTelecom().getCountryCode(),
											participantDetails.getPatientDetails().getTelecom().getNumber(),
											participantDetails.getPatientDetails().getTelecom()
													.getTelecomTypeIdentifier()),
							participantDetails.getPatientDetails().getEmailId()));
				}
				if (participantDetails.getDoctorDetails() != null) {
					details.setDoctorDetails(new DoctorGeneralDetails(
							participantDetails.getDoctorDetails().getSpecialties() == null ? null
									: participantDetails.getDoctorDetails().getSpecialties(),
							participantDetails.getDoctorDetails().getRole() != null
									&& participantDetails.getDoctorDetails().getRole().getId() != null
											? new DoctorRoleDetails(
													participantDetails.getDoctorDetails().getRole().getId(),
													participantDetails.getDoctorDetails().getRole().getName(),
													participantDetails.getDoctorDetails().getRole().getIdentifier())
											: null,
							null, null));

				}
				patList.add(details);
			}
		}

		OldAppointmentDto res = new OldAppointmentDto(request.getId(), docDetails.getParticipantId(),
				docDetails.getParticipantName(), null, null, patDetails.getParticipantId(),
				patDetails.getParticipantName(), request.getRefDoctorId(), request.getRefDoctorName(),
				request.getTransferSourseId(), null, patList,
				request.getAppointmentCategory() == null ? null : request.getAppointmentCategory().getIdentifierCode(),
				request.getAppointmentType() == null ? null : request.getAppointmentType().getIdentifierCode(),
				request.getAppointmentStatus() == null ? null : request.getAppointmentStatus().getNameEn(), null, null,
				request.getPayerType(), request.getVisitType(),
				request.getAppointmentPriority() == null ? null : request.getAppointmentPriority().getIdentifierCode(),
				request.getAppointmentConductMode() == null ? null : request.getAppointmentConductMode(),
				request.getAppointmentBookingSource() == null ? null
						: request.getAppointmentBookingSource().getIdentifierCode(),
				request.getAppointmentReferralCategory() == null ? null
						: request.getAppointmentReferralCategory().getIdentifierCode(),
				request.getAppointmentBookingMode() == null ? null
						: request.getAppointmentBookingMode().getIdentifierCode(),
				request.getStartDateEpoc(), request.getEndDateEpoc(), request.getDurationinMinutes(),
				request.getInstructions(), request.getAdministrativeNotes(), null, request.getReconfirmedReason(),
				request.getCancelledReason(), request.getIsWaitingList(), request.getExternalAppointmentId(),
				request.getRemarks() == null || request.getRemarks().size() == 0 ? null : request.getRemarks().get(0),
				request.getIsNewVisit(), null, null,
				request.getAppointmentType() == null ? null : request.getAppointmentType().getIdentifierCode(),
				request.getId(), null, null, null, null, null, null, request.getTeleconsultChannelId(), request.getSlotId(), null,
				null, request.getIsReschedule(), null, null, request.getIsVip(), request.getVisitDetails(),
				request.getIsEmergency(), null, null, null, null, null, null, null, null, null, null, null,null,null);
		return res;

	}

	private List<ParticipantDetails> getParticipants(OldAppointmentDto request) {
		List<ParticipantDetails> particpantCalendar = new ArrayList<>();
		if (request.getParticpantCalendar() != null && request.getParticpantCalendar().size() > 0) {
			for (ParticpantCalendar participantDetails : request.getParticpantCalendar()) {

				try {
					ParticipantDetails details = new ParticipantDetails(
							participantDetails.getParticipantId() == null ? null
									: participantDetails.getParticipantId(),
							participantDetails.getSalutationName() != null
									? (participantDetails.getSalutationName() + " "
											+ participantDetails.getParticipantName() == null ? ""
													: participantDetails.getParticipantName())
									: participantDetails.getParticipantName(),
							participantDetails.getSpecialityIdentifier() == null ? null
									: participantDetails.getSpecialityIdentifier(),
							new SystemMasterDTO(null, null, null,
									participantDetails.getAppointmentParticipantType() == null ? null
											: participantDetails.getAppointmentParticipantType().getIdentifierCode()),
							participantDetails.getAppointmentParticipantType() == null ? null
									: participantDetails.getAppointmentParticipantType().getIdentifierCode()
											.equals("Patient")
													? participantDetails.getPatientDetails() == null ? null
															: new PatientGeneralDetails(null, null, null, null, null,
																	participantDetails.getPatientDetails()
																			.getGender() == null
																					? null
																					: participantDetails
																							.getPatientDetails()
																							.getGender(),
																	participantDetails.getPatientDetails()
																			.getDob() == null
																					? null
																					: participantDetails
																							.getPatientDetails()
																							.getDob(),
																	null, null,
																	participantDetails.getPatientDetails()
																			.getTelecom() == null
																					? null
																					: new NewTelecomDTO(
																							participantDetails
																									.getPatientDetails()
																									.getTelecom()
																									.getCountryCode() == null
																											? null
																											: participantDetails
																													.getPatientDetails()
																													.getTelecom()
																													.getCountryCode(),
																							participantDetails
																									.getPatientDetails()
																									.getTelecom()
																									.getNumber() == null
																											? null
																											: participantDetails
																													.getPatientDetails()
																													.getTelecom()
																													.getNumber(),
																							participantDetails
																									.getPatientDetails()
																									.getTelecom()
																									.getTelecomTypeIdentifier() == null
																											? null
																											: participantDetails
																													.getPatientDetails()
																													.getTelecom()
																													.getTelecomTypeIdentifier()),
																	participantDetails.getPatientDetails()
																			.getEmailId() == null
																					? null
																					: participantDetails
																							.getPatientDetails()
																							.getEmailId())
													: null,
							participantDetails.getAppointmentParticipantType() == null ? null
									: participantDetails.getAppointmentParticipantType().getIdentifierCode()
											.equals("Practitioner")
													? participantDetails.getDoctorDetails() == null ? null
															: new DoctorGeneralDetails(
																	participantDetails.getDoctorDetails()
																			.getSpecialties() == null ? null
																					: participantDetails
																							.getDoctorDetails()
																							.getSpecialties(),
																	participantDetails.getDoctorDetails()
																			.getRole() == null
																					? null
																					: new DoctorRoleDetails(
																							participantDetails
																									.getDoctorDetails()
																									.getRole()
																									.getId() == null
																											? null
																											: participantDetails
																													.getDoctorDetails()
																													.getRole()
																													.getId(),
																							participantDetails
																									.getDoctorDetails()
																									.getRole()
																									.getName() == null
																											? null
																											: participantDetails
																													.getDoctorDetails()
																													.getRole()
																													.getName(),
																							participantDetails
																									.getDoctorDetails()
																									.getRole()
																									.getIdentifier() == null
																											? null
																											: participantDetails
																													.getDoctorDetails()
																													.getRole()
																													.getIdentifier()),
																	participantDetails.getDoctorDetails()
																			.getEmailId() == null
																					? null
																					: participantDetails
																							.getDoctorDetails()
																							.getEmailId(),
																	participantDetails.getDoctorDetails()
																			.getTelecom() == null
																					? null
																					: new NewTelecomDTO(
																							participantDetails
																									.getDoctorDetails()
																									.getTelecom()
																									.getCountryCode() == null
																											? null
																											: participantDetails
																													.getDoctorDetails()
																													.getTelecom()
																													.getCountryCode(),
																							participantDetails
																									.getDoctorDetails()
																									.getTelecom()
																									.getNumber() == null
																											? null
																											: participantDetails
																													.getDoctorDetails()
																													.getTelecom()
																													.getNumber(),
																							participantDetails
																									.getDoctorDetails()
																									.getTelecom()
																									.getTelecomTypeIdentifier() == null
																											? null
																											: participantDetails
																													.getDoctorDetails()
																													.getTelecom()
																													.getTelecomTypeIdentifier()))
													: null,
							participantDetails.getName(), participantDetails.getSalutationName(),
							participantDetails.getAliasName() == null ? null : participantDetails.getAliasName());

					particpantCalendar.add(details);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return particpantCalendar;
	}

	private List<Remarks> getRemarks(OldAppointmentDto request) {
		return request.getRemarks() == null ? new ArrayList<>() : Collections.singletonList(request.getRemarks());
	}

	public Appointment mapToAppointmentDocument(OldAppointmentDto request, TokenPayLoad tokenPayload) {

		return new Appointment(request.getId() == null ? null : request.getId(), getParticipants(request),
				request.getAppointmentCategory() == null ? null
						: new SystemMasterDTO(null, null, null, request.getAppointmentCategory()),
				request.getAppointmentType() == null ? null
						: new SystemMasterDTO(null, null, null, request.getAppointmentType()),
				request.getAppointmentStatus() == null ? null : new SystemMasterDTO((long) 39, "Open", "Open", "O"),
				request.getPayerType() == null ? null
						: new SystemMasterDTO(request.getPayerType().getId(), request.getPayerType().getNameEn(),
								request.getPayerType().getDescription(), request.getPayerType().getIdentifierCode()),
				request.getVisitType() == null ? null
						: new SystemMasterDTO(request.getVisitType().getId(), request.getVisitType().getNameEn(),
								request.getVisitType().getDescription(), request.getVisitType().getIdentifierCode()),
				request.getAppointmentPriority() == null ? null
						: new SystemMasterDTO(null, null, null, request.getAppointmentPriority()),
				request.getAppointmentConductMode() == null ? null
						: new SystemMasterDTO(request.getAppointmentConductMode().getId(),
								request.getAppointmentConductMode().getNameEn(),
								request.getAppointmentConductMode().getDescription(),
								request.getAppointmentConductMode().getIdentifierCode()),
				request.getAppointmentBookingSource() == null ? null
						: new SystemMasterDTO(null, null, null, request.getAppointmentBookingSource()),
				request.getAppointmentReferralCategory() == null ? null
						: new SystemMasterDTO(null, null, null, request.getAppointmentReferralCategory()),
				request.getAppointmentBookingMode() == null ? null
						: new SystemMasterDTO(null, null, null, request.getAppointmentBookingMode()),
				new Date(request.getAppointmentStartDate()), new Date(request.getAppointmentEndDate()),
				request.getDurationinMinutes(), request.getInstructions(), request.getAdministrativeNotes(),
				request.getReconfirmedOn() == null || request.getReconfirmedOn().equals("") ? null
						: AppUtil.parseDateTime(request.getReconfirmedOn(), "yyyy-MM-dd"),
				request.getReconfirmedReason(), request.getIsWaitingList() == null ? false : request.getIsWaitingList(),
				request.getExternalAppointmentId(), request.getCancelledReason(), getRemarks(request),
				request.getIsNewVisit(), request.getAppointmentSite(), request.getClinicalactivitydetails(), null,
				new CustomerTransactionBase(
						tokenPayload.getCustomerBusinessId() == null ? request.getCustomerBusinessId()
								: tokenPayload.getCustomerBusinessId(),
						tokenPayload.getCustomerBusinessId() == null ? request.getCustomerBusinessId()
								: tokenPayload.getCustomerBusinessId(),
						null),
				request.getSlotId(), request.getAppointmentStartDate(), request.getAppointmentEndDate(), null, null,
				null, request.getTeleconsultChannelId(), null, null, null, request.getIsReschedule(),
				request.getIsEmergency(), request.getIsVip(), request.getVisitDetails(), request.getIsReferralAppointment());

	}

	public FailedEventsGetDto mapToMessageEventDto(MessageEvent request) {
		FailedEventsGetDto failedEve = new FailedEventsGetDto(request.getId(), request.getTopic(), request.getRequest(),
				request.getEventStatus().name(), request.getErrorMessage(),
				request.getTransactionBase().getCreatedOn());

		return failedEve;

	}

	public List<DoctorListDto> getDoctorList(List<ParticipantScheduleDetails> availabilityList,
			List<ParticipantScheduleDetails> nonAvailabilityList, Date date) throws ApplicationException {

		List<DoctorListDto> doctorList = new ArrayList<>();
		for (ParticipantScheduleDetails participantScheduleDetails : availabilityList) {

			String rruleString = participantScheduleDetails.getCustomScheduleDto().getRecurrenceRule();

			boolean isTodayAvailable = false;
			Date nextAvailable = null;
			int totalSlots = 0;

			Date fromDate = new Date(participantScheduleDetails.getCustomScheduleDto().getScheduleFrom());
			Calendar calendarFrom = Calendar.getInstance();
			calendarFrom.setTime(fromDate);
			TimeDto fromTime = AppUtil.getTime(participantScheduleDetails.getCustomScheduleDto().getScheduleFromTime());
			calendarFrom.add(Calendar.HOUR_OF_DAY, fromTime.getHour());
			calendarFrom.add(Calendar.MINUTE, fromTime.getMinutes());
			calendarFrom.add(Calendar.SECOND, 30);

			Date endDt = new Date(participantScheduleDetails.getCustomScheduleDto().getScheduleTo());
			Calendar calendarTo = Calendar.getInstance();
			calendarTo.setTime(endDt);
			TimeDto endTime = AppUtil.getTime(participantScheduleDetails.getCustomScheduleDto().getScheduleToTime());
			calendarTo.add(Calendar.HOUR_OF_DAY, endTime.getHour());
			calendarTo.add(Calendar.MINUTE, endTime.getMinutes());
			calendarTo.add(Calendar.SECOND, 30);

			// Generate occurrences from startDate
			try {
				RRule rrule = new RRule(rruleString);
				Recur recur = rrule.getRecur();

				DateTime start = new DateTime(calendarFrom.getTime());
				DateTime end = new DateTime(calendarTo.getTime());
				DateList dates = recur.getDates(start, end, Value.DATE_TIME);

				for (net.fortuna.ical4j.model.Date occurrence : dates) {
					Date occurrenceDate = new Date(occurrence.getTime());
					if (occurrenceDate.after(calendarTo.getTime())) {
						break;
					}
					if (occurrenceDate.equals(date)) {
						isTodayAvailable = true;
					}
					if (nextAvailable == null && occurrenceDate.after(date)) {
						nextAvailable = occurrenceDate;
					}
					totalSlots++;
				}
			} catch (Exception e) {
				throw new FailedException(e.getMessage());
			}
			// Check against leaves
			boolean isOnLeave = false;
			for (ParticipantScheduleDetails leave : nonAvailabilityList) {
				if (leave.getParticipantId().equals(participantScheduleDetails.getParticipantId())) {
					if (AppUtil.convertEpochToDate(leave.getCustomScheduleDto().getScheduleFrom()).before(date)
							&& AppUtil.convertEpochToDate(leave.getCustomScheduleDto().getScheduleTo()).after(date)) {
						isOnLeave = true;
						break;
					}
				}
			}

			DoctorListDto doctorListDto = new DoctorListDto(Long.valueOf(participantScheduleDetails.getParticipantId()),
					participantScheduleDetails.getParticipantName(),
					participantScheduleDetails.getScheduledParticipant().getParticipantUser().getGender(),
					participantScheduleDetails.getScheduledParticipant().getParticipantUser().getSpecialties()
							.size() > 0
									? participantScheduleDetails.getScheduledParticipant().getParticipantUser()
											.getSpecialties().get(0)
									: null,
					participantScheduleDetails.getScheduledParticipant().getParticipantUser().getProfilePhoto(),
					(isTodayAvailable && !isOnLeave), nextAvailable, totalSlots);
			doctorList.add(doctorListDto);
		}
		Set<Long> seenIds = new HashSet<>();
		return doctorList.stream().filter(detail -> seenIds.add(detail.getCoreUserId())).collect(Collectors.toList());

	}

//	private boolean isDoctorAvailable(List<ParticipantScheduleDetails> nonAvailabilityList, String participantId,
//			Long date) {
//		boolean isNotAvailable = false;
//
//		isNotAvailable = nonAvailabilityList.stream().filter(psd -> participantId.equals(psd.getParticipantId()))
//				.anyMatch(detail -> {
//					Long scheduleFrom = detail.getCustomScheduleDto().getScheduleFrom();
//					Long scheduleTo = detail.getCustomScheduleDto().getScheduleTo();
//					return scheduleFrom != null && scheduleTo != null && date >= scheduleFrom && date <= scheduleTo;
//				});
//
//		return !isNotAvailable;
//	}

	public DoctorSlotUtilizationDTO mapToDoctorSlotUtilization(Appointment appointment) {
		return new DoctorSlotUtilizationDTO(appointment.getId(), appointment.getAppointmentSite().getId(),
				appointment.getAppointmentSite().getSiteName(),
				appointment.getParticpantCalendar().get(0).getParticipantId(),
				appointment.getParticpantCalendar().get(0).getParticipantName(),
				appointment.getParticpantCalendar().get(0).getAppointmentParticipantType().getIdentifierCode(),
				appointment.getAppointmentCategory().getIdentifierCode(),
				appointment.getAppointmentType().getIdentifierCode(),
				appointment.getAppointmentBookingSource().getIdentifierCode(), appointment.getStartDate(),
				appointment.getEndDate(), appointment.getDurationinMinutes(), appointment.getReconfirmedReason(),
				appointment.getTransactionBase().getCustomerId(),
				appointment.getTransactionBase().getCustomerBusinessId(),
				appointment.getTransactionBase().getCreatedOn());
	}

}
