package com.lifetrenz.lths.appointment.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.common.app.exception.FailedException;
import com.lifetrenz.lths.appointment.common.app.exception.NotFoundException;
import com.lifetrenz.lths.appointment.common.builders.MessageEventBuilder;
import com.lifetrenz.lths.appointment.common.enums.KafkaTopic;
import com.lifetrenz.lths.appointment.common.enums.MessageEventStatus;
import com.lifetrenz.lths.appointment.common.enums.MessageRequestStatus;
import com.lifetrenz.lths.appointment.common.enums.SlotReservation;
import com.lifetrenz.lths.appointment.common.enums.SlotStatus;
import com.lifetrenz.lths.appointment.common.util.AppUtils;
import com.lifetrenz.lths.appointment.common.util.KafkaTopics;
import com.lifetrenz.lths.appointment.dto.AppointmentStatusDTO;
import com.lifetrenz.lths.appointment.dto.AuditEventAppoinmentDto;
import com.lifetrenz.lths.appointment.dto.AuditEventDto;
import com.lifetrenz.lths.appointment.dto.AuditEventType;
import com.lifetrenz.lths.appointment.dto.CustomerTransactionAttributeDTO;
import com.lifetrenz.lths.appointment.dto.DoctorListDto;
import com.lifetrenz.lths.appointment.dto.DoctorSiteMobDto;
import com.lifetrenz.lths.appointment.dto.DoctorSlots;
import com.lifetrenz.lths.appointment.dto.EventAction;
import com.lifetrenz.lths.appointment.dto.KafkaTransactionBase;
import com.lifetrenz.lths.appointment.dto.LtAuditEventDto;
import com.lifetrenz.lths.appointment.dto.OnlineDoctors;
import com.lifetrenz.lths.appointment.dto.PageDTO;
import com.lifetrenz.lths.appointment.dto.PagenationParticipantScheduleDetailsDto;
import com.lifetrenz.lths.appointment.dto.ParticipantScheduleDetailsGetDTO;
import com.lifetrenz.lths.appointment.dto.ParticipantScheduleGetDto;
import com.lifetrenz.lths.appointment.dto.ParticipantScheduleResponseDto;
import com.lifetrenz.lths.appointment.dto.PersonNameMobDto;
import com.lifetrenz.lths.appointment.dto.Qualification;
import com.lifetrenz.lths.appointment.dto.QualificationDTO;
import com.lifetrenz.lths.appointment.dto.Registration;
import com.lifetrenz.lths.appointment.dto.ScheduleMobDto;
import com.lifetrenz.lths.appointment.dto.ScheduledParticipantGetDTO;
import com.lifetrenz.lths.appointment.dto.ScheduledParticipantMobDto;
import com.lifetrenz.lths.appointment.dto.Slot;
import com.lifetrenz.lths.appointment.dto.Specialization;
import com.lifetrenz.lths.appointment.dto.SystemMasterDTO;
import com.lifetrenz.lths.appointment.dto.TelecomDTO;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.dto.UserUpdateProfileDTO;
import com.lifetrenz.lths.appointment.dto.WorkExperienceDTO;
import com.lifetrenz.lths.appointment.enums.AuditEntityType;
import com.lifetrenz.lths.appointment.mapper.AppointmentMapper;
import com.lifetrenz.lths.appointment.mapper.DoctorMapper;
import com.lifetrenz.lths.appointment.mapper.EventsMapper;
import com.lifetrenz.lths.appointment.mapper.ScheduleMapper;
import com.lifetrenz.lths.appointment.model.collection.Appointment;
import com.lifetrenz.lths.appointment.model.collection.CalendarScheduleType;
import com.lifetrenz.lths.appointment.model.collection.ParticipantScheduleDetails;
import com.lifetrenz.lths.appointment.model.collection.ScheduledParticipant;
import com.lifetrenz.lths.appointment.model.collection.SchedulerEvent;
import com.lifetrenz.lths.appointment.model.collection.User;
import com.lifetrenz.lths.appointment.model.enums.ScheduleEventStatus;
import com.lifetrenz.lths.appointment.model.enums.SlotCategory;
import com.lifetrenz.lths.appointment.model.events.MessageEvent;
import com.lifetrenz.lths.appointment.model.value_object.Address;
import com.lifetrenz.lths.appointment.model.value_object.Name;
import com.lifetrenz.lths.appointment.model.value_object.SystemMaster;
import com.lifetrenz.lths.appointment.model.value_object.UserSite;
import com.lifetrenz.lths.appointment.repository.command.IAppointmentRepository;
import com.lifetrenz.lths.appointment.repository.command.IAppointmentSchedularRepository;
import com.lifetrenz.lths.appointment.repository.command.IScheduledParticipantRepository;
import com.lifetrenz.lths.appointment.repository.command.ISchedulerEventRepository;
import com.lifetrenz.lths.appointment.repository.query.AppointmentRepository;
import com.lifetrenz.lths.appointment.service.AppointmentSchedulerService;
import com.lifetrenz.lths.appointment.service.AuditService;
import com.lifetrenz.lths.appointment.service.MessageEventService;
import com.lifetrenz.lths.appointment.service.ParticipantScheduleValidationService;
import com.lifetrenz.lths.appointment.service.ProducerService;
import com.lifetrenz.lths.appointment.service.SchedulerEventService;
import com.lifetrenz.lths.appointment.util.AppUtil;

@Component
public class AppointmentSchedularServiceImpl implements AppointmentSchedulerService {

	private static final Logger log = LoggerFactory.getLogger(AppointmentSchedularServiceImpl.class);

	@Autowired
	IAppointmentSchedularRepository appointmentSchedularRepository;

	@Autowired
	IScheduledParticipantRepository scheduledParticipantRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	SchedulerEventService schedulerEventService;

	@Autowired
	ParticipantScheduleValidationService participantScheduleValidationService;

	@Autowired
	DoctorMapper doctorMapper;

	@Autowired
	ScheduleMapper scheduleMapper;

	@Autowired
	ProducerService producerService;

	@Autowired
	MessageEventBuilder messageEventBuilder;

	@Autowired
	MessageEventService messageEventService;

	@Autowired
	AppointmentRepository appointmentRepository;

	@Autowired
	IAppointmentRepository appointmentRepo;

	@Autowired
	ISchedulerEventRepository schedulerEventRepository;

	@Autowired
	EventsMapper eventsMapper;

	@Autowired
	MongoOperations mongoOperations;

	@Autowired
	AppointmentMapper appointmentMapper;

	@Autowired
	AuditService auditService;

	@Override
	public ParticipantScheduleResponseDto saveAppointmentSchedular(ParticipantScheduleDetails appointmentSchedular,
			TokenPayLoad tokenPayload, String path) throws ApplicationException {
		try {
			ScheduledParticipant scheduledParticipant = null;
			String status = "";
			if (tokenPayload != null) {
				appointmentSchedular.setCreatedBy(tokenPayload.getName());
			}

			if (appointmentSchedular.getCreatedBy() != null) {
				appointmentSchedular.setCreatedOn(new Date());
			}
			if (appointmentSchedular.getParticipantId() != null) {
				if (scheduledParticipantRepository
						.existsByParticipantIdAndParticipantType_IdentifierCodeAndConductingSiteIdAndCalendarType_IdentifierCode(
								Long.parseLong(appointmentSchedular.getParticipantId()),
								appointmentSchedular.getParticipantType().getIdentifierCode(),
								Long.parseLong(appointmentSchedular.getConductingSiteId()),
								appointmentSchedular.getCalendarType().getIdentifierCode())) {

					scheduledParticipant = scheduledParticipantRepository
							.findByParticipantIdAndParticipantType_IdentifierCodeAndConductingSiteIdAndCalendarType_IdentifierCode(
									Long.parseLong(appointmentSchedular.getParticipantId()),
									appointmentSchedular.getParticipantType().getIdentifierCode(),
									Long.parseLong(appointmentSchedular.getConductingSiteId()),
									appointmentSchedular.getCalendarType().getIdentifierCode());

					appointmentSchedular.getScheduledParticipant().setId(scheduledParticipant.getId());
					appointmentSchedular.getScheduledParticipant()
							.setCustomerBusinessId(appointmentSchedular.getCustomerBusinessId());
					appointmentSchedular.getScheduledParticipant().setActive(true);
					appointmentSchedular.getScheduledParticipant().setCustomerId(appointmentSchedular.getCustomerId());
					appointmentSchedular.getScheduledParticipant().setCreatedBy(appointmentSchedular.getCreatedBy());
					appointmentSchedular.getScheduledParticipant().setCreatedOn(new Date());
					appointmentSchedular.getScheduledParticipant().setSiteId(appointmentSchedular.getSiteId());
					appointmentSchedular.getScheduledParticipant()
							.setCreatedById(appointmentSchedular.getCreatedById());
					appointmentSchedular.getScheduledParticipant()
							.setConductingSiteId(Long.parseLong(appointmentSchedular.getConductingSiteId()));
					appointmentSchedular.getScheduledParticipant()
							.setCalendarType(appointmentSchedular.getCalendarType());
					appointmentSchedular.getReason();

					Name name = new Name();
					if (appointmentSchedular.getScheduledParticipant().getParticipantUser() != null) {
						if (appointmentSchedular.getScheduledParticipant().getParticipantUser().getName()
								.getFirstname() != null) {
							name.setFirstname(appointmentSchedular.getScheduledParticipant().getParticipantUser()
									.getName().getFirstname());
						} else {
							name.setFirstname("");
						}
						if (appointmentSchedular.getScheduledParticipant().getParticipantUser().getName()
								.getMiddlename() != null) {
							name.setMiddlename(appointmentSchedular.getScheduledParticipant().getParticipantUser()
									.getName().getMiddlename());
						} else {
							name.setMiddlename("");
						}
						if (appointmentSchedular.getScheduledParticipant().getParticipantUser().getName()
								.getLastname() != null) {
							name.setLastname(appointmentSchedular.getScheduledParticipant().getParticipantUser()
									.getName().getLastname());
						} else {
							name.setLastname("");
						}
						appointmentSchedular.getScheduledParticipant().getParticipantUser().setName(name);
					}

//					scheduledParticipant = scheduledParticipantRepository
//							.save(appointmentSchedular.getScheduledParticipant());

					if (appointmentSchedular.getScheduledParticipant().getLocation() != null) {
						scheduledParticipant.setLocation(appointmentSchedular.getScheduledParticipant().getLocation());
					}
					if (appointmentSchedular.getScheduledParticipant().getEquipment() != null) {
						scheduledParticipant
								.setEquipment(appointmentSchedular.getScheduledParticipant().getEquipment());
					}
					if (appointmentSchedular.getScheduledParticipant().getSpeciality() != null) {
						scheduledParticipant
								.setSpeciality(appointmentSchedular.getScheduledParticipant().getSpeciality());
					}

					appointmentSchedular.setScheduledParticipant(scheduledParticipant);

				} else {

					scheduledParticipant = new ScheduledParticipant();
					if (appointmentSchedular.getScheduledParticipant().getParticipantUser() != null) {

						Name name = new Name();
						if (appointmentSchedular.getScheduledParticipant().getParticipantUser().getName()
								.getFirstname() != null) {
							name.setFirstname(appointmentSchedular.getScheduledParticipant().getParticipantUser()
									.getName().getFirstname());
						} else {
							name.setFirstname("");
						}
						if (appointmentSchedular.getScheduledParticipant().getParticipantUser().getName()
								.getMiddlename() != null) {
							name.setMiddlename(appointmentSchedular.getScheduledParticipant().getParticipantUser()
									.getName().getMiddlename());
						} else {
							name.setMiddlename("");
						}
						if (appointmentSchedular.getScheduledParticipant().getParticipantUser().getName()
								.getLastname() != null) {
							name.setLastname(appointmentSchedular.getScheduledParticipant().getParticipantUser()
									.getName().getLastname());
						} else {
							name.setLastname("");
						}
						appointmentSchedular.getScheduledParticipant().getParticipantUser().setName(name);

						scheduledParticipant.setParticipantUser(
								appointmentSchedular.getScheduledParticipant().getParticipantUser());
					}
					if (appointmentSchedular.getScheduledParticipant().getLocation() != null) {
						scheduledParticipant.setLocation(appointmentSchedular.getScheduledParticipant().getLocation());
					}
					if (appointmentSchedular.getScheduledParticipant().getEquipment() != null) {
						scheduledParticipant
								.setEquipment(appointmentSchedular.getScheduledParticipant().getEquipment());
					}

					scheduledParticipant.setScheduleParticipantUser(
							appointmentSchedular.getScheduledParticipant().getScheduleParticipantUser());
					SystemMaster sys = new SystemMaster();
					sys.setId(appointmentSchedular.getParticipantType().getId());
					sys.setIdentifierCode(appointmentSchedular.getParticipantType().getIdentifierCode());
					sys.setDescription(appointmentSchedular.getParticipantType().getDescription());
					sys.setNameEn(appointmentSchedular.getParticipantType().getName());
					scheduledParticipant.setParticipantType(appointmentSchedular.getParticipantType());

					scheduledParticipant.setParticipantId(Long.parseLong(appointmentSchedular.getParticipantId()));

					scheduledParticipant.setActive(true);
					scheduledParticipant.setCreatedBy(appointmentSchedular.getCreatedBy());
					scheduledParticipant.setCreatedOn(new Date());
					scheduledParticipant.setCustomerId(appointmentSchedular.getCustomerId());
					scheduledParticipant.setCustomerBusinessId(appointmentSchedular.getCustomerBusinessId());
					scheduledParticipant.setSiteId(appointmentSchedular.getSiteId());
					scheduledParticipant
							.setConductingSiteId(Long.parseLong(appointmentSchedular.getConductingSiteId()));
					scheduledParticipant.setCalendarType(appointmentSchedular.getCalendarType());

					scheduledParticipant = this.scheduledParticipantRepository.save(scheduledParticipant);

					appointmentSchedular.setScheduledParticipant(scheduledParticipant);

				}

			}

			try {

				List<String> scheduleTypes = new ArrayList<>();

				if (appointmentSchedular.getScheduleType().equals("NS")) {
					scheduleTypes.add(appointmentSchedular.getScheduleType());
				} else {
					scheduleTypes.add(appointmentSchedular.getScheduleType());
					// scheduleTypes.add("AS");

				}

				List<ParticipantScheduleDetails> confList = this.getConfiguration(
						Long.valueOf(appointmentSchedular.getConductingSiteId()),
						appointmentSchedular.getCalendarType().getIdentifierCode(),
						appointmentSchedular.getParticipantType().getIdentifierCode(),
						appointmentSchedular.getParticipantId(), scheduleTypes, appointmentSchedular.getCustomerId(),
						path);

				if (this.participantScheduleValidationService.scheduleValidation(appointmentSchedular, confList)) {
					status = "406";
				} else {
					status = "200";

					if (appointmentSchedular.getScheduleType().equals("NS")) {
						List<Appointment> appt = this.appointmentRepository.getNonAvab(
								Long.valueOf(appointmentSchedular.getConductingSiteId()),
								appointmentSchedular.getCustomScheduleDto().getScheduleFrom(),
								appointmentSchedular.getCustomScheduleDto().getScheduleTo(),
								appointmentSchedular.getParticipantId() == null ? null
										: appointmentSchedular.getParticipantId(),
								null,
								appointmentSchedular.getParticipantType() == null ? null
										: String.valueOf(appointmentSchedular.getParticipantType().getIdentifierCode()),
								null, "O", null);
						appt.forEach(item -> {
							item.setAppointmentStatus(new SystemMasterDTO((long) 1656, "Pending Reconciliation",
									"Pending Reconciliation", "PENDING-RECON"));

							try {
								List<SchedulerEvent> scheEvent = this.schedulerEventService
										.getScheduleEventByAppointmentId(item.getId());
								if (scheEvent != null && scheEvent.size() > 0) {
									for (SchedulerEvent appEvent : scheEvent) {
										if (appEvent.getEventData() != null) {
											appEvent.getEventData().setEventStatus(ScheduleEventStatus.PENDING_RECON);
										}

									}
									this.schedulerEventRepository.saveAll(scheEvent);
								}
							} catch (ApplicationException e) {

								e.printStackTrace();
							}

						});
						if (appt.size() > 0) {
							this.appointmentRepo.saveAll(appt);
							this.impactedAppointments(appt, tokenPayload, path, true);
						}
					}

					appointmentSchedular = this.appointmentSchedularRepository.save(appointmentSchedular);
					List<SchedulerEvent> scheduleEventList;
					try {

						String request = appointmentSchedular.getScheduleConfig() + "~" + appointmentSchedular.getId();
						String[] resultRequest = request.split("~");
						if (resultRequest.length > 0) {
							scheduleEventList = Arrays
									.asList(new ObjectMapper().readValue(resultRequest[0], SchedulerEvent[].class));

							if (appointmentSchedular.getCalendarType().getIdentifierCode().equals("Consult-Calendar")) {
								this.createConsultCalendar(scheduleEventList, resultRequest, appointmentSchedular,
										path);

//							} else if (appointmentSchedular.getCalendarType().getIdentifierCode()
//									.equals("Spec-Calendar")) {
//								this.createConsultCalendar(scheduleEventList, resultRequest, appointmentSchedular,
//										path);
							} else {
								this.createServiceCalendar(scheduleEventList, resultRequest, appointmentSchedular,
										path);
							}

						}

						if (appointmentSchedular.getScheduledParticipant() != null
								&& appointmentSchedular.getScheduledParticipant().getParticipantUser() != null
								&& appointmentSchedular.getScheduledParticipant().getParticipantUser()
										.getPortalEnable() != null
								&& appointmentSchedular.getScheduledParticipant().getParticipantUser()
										.getPortalEnable()) {
							this.publishKafkaSchedule(appointmentSchedular, scheduledParticipant, tokenPayload, path);
						}

					} catch (Exception e) {
						throw new FailedException("Failed to save configuration.");
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			ParticipantScheduleResponseDto response = null;
			try {
				response = new ParticipantScheduleResponseDto(appointmentSchedular.getId(),
						appointmentSchedular.getCalendarType(), appointmentSchedular.getParticipantType(),
						appointmentSchedular.getConductingSiteId(), appointmentSchedular.getParticipantId(),
						appointmentSchedular.getParticipantName(), appointmentSchedular.getMaximumWaitingList(),
						appointmentSchedular.getIsActive(), appointmentSchedular.getDuration(),
						appointmentSchedular.getScheduleConfig(), appointmentSchedular.getSlotType(),

						appointmentSchedular.getCustomScheduleDto(), appointmentSchedular.getConfigBreak(),
						appointmentSchedular.getScheduledParticipant(), status, appointmentSchedular.getReason(),
						appointmentSchedular.getVisitType(), appointmentSchedular.getConductingSiteName(),
						appointmentSchedular.getSpeciality());
			} catch (Exception e) {
			}

			CustomerTransactionAttributeDTO custAttr = new CustomerTransactionAttributeDTO(true,
					tokenPayload.getCoreUserId(), new Date(), null, null, tokenPayload.getCustomerBusinessId(),
					tokenPayload.getCustomerId(), appointmentSchedular.getSiteId(), null);

			AuditEventAppoinmentDto auditEventDto = new AuditEventAppoinmentDto(
					AuditEventType.APPOINTMENT_CONFIGURATION, tokenPayload,
					AppUtil.convertJsonToString(appointmentSchedular));
//			MessageEvent messageEvent = this.eventsMapper.convertToMessageEvent(AppUtil.convertJsonToString(auditEventDto),
//					1, KafkaTopics.LT_AUDIT_APPOINMENT_EVENT, custAttr, tokenPayload.getPreferred_username());
			//
//			try {
//				this.producerService.publishToKafka(1, KafkaTopics.LT_AUDIT_APPOINMENT_EVENT,
//						AppUtil.convertJsonToString(auditEventDto), messageEvent);
//			} catch (Exception e) {
//			},

			try {
				this.auditAppointmentEvent(auditEventDto, custAttr, tokenPayload, path);
			} catch (Exception e) {
				throw new FailedException("Failed to produce kafka to LT_AUDIT_APPOINMENT_EVENT !");
			}

			LtAuditEventDto auditDto = new LtAuditEventDto(AuditEventType.APPOINTMENT_CONFIGURATION,
					AppUtil.convertJsonToString(appointmentSchedular), AuditEntityType.SETTINGS.value,
					String.valueOf(appointmentSchedular.getId()), EventAction.ADD, true, tokenPayload.getName(),
					tokenPayload.getCoreUserId(), new Date(), tokenPayload.getCustomerBusinessId(),
					tokenPayload.getCustomerId(), null, "Appointment Configuration", "", "",
					"Appointment Configuration saved successfully.");
			try {
				this.auditService.addtoAudit(auditDto, custAttr, path);
			} catch (Exception e) {
				throw new FailedException("Failed to produce kafka to LT_AUDIT_TRIAL_EVENTS !");
			}
			return response;
		} catch (Exception e) {
			// TODO: handle exception
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
			return null;
		}

	}

	private void createConsultCalendar(List<SchedulerEvent> scheduleEventList, String[] resultRequest,
			ParticipantScheduleDetails appointmentSchedular, String path) {
		for (SchedulerEvent itemObj : scheduleEventList) {

			if (itemObj.getEventData().getIsBlock()) {

				SchedulerEvent item = new SchedulerEvent();

				item = itemObj;
				item.setId(null);
				Date end = itemObj.getEventData().getEndTime();
				item.setReferrenceId(resultRequest[1]);

				Calendar calendarStart = Calendar.getInstance();
				calendarStart.setTime(itemObj.getEventData().getStartTime());
				calendarStart.set(Calendar.SECOND, 1);
				item.getEventData().setStartTime(calendarStart.getTime());
				item.getEventData().setEndTime(end);

				switch (appointmentSchedular.getScheduleType()) {
				case "AS":
					item.setScheduleType(CalendarScheduleType.AVAILABILITY);
					break;
				case "NS":
					item.setScheduleType(CalendarScheduleType.NON_AVAILABILITY);
					break;
				case "ADS":
					item.setScheduleType(CalendarScheduleType.ADHOC);
					break;
				default:
					break;
				}

				try {
					this.schedulerEventService.saveScheuleEvent(item, null,
							path + "/" + this.getClass().getSimpleName());
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				long consultigDuration = AppUtil.getTimeDifferenceByDate(itemObj.getEventData().getStartTime(),
						itemObj.getEventData().getEndTime());

				int noOfSlots = (int) (consultigDuration / appointmentSchedular.getDuration());

				Date start = itemObj.getEventData().getStartTime();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(start);
				calendar.add(Calendar.MINUTE, Integer.parseInt(String.valueOf(appointmentSchedular.getDuration())));
				Date end = calendar.getTime();
				for (int i = 0; i < noOfSlots; i++) {
					SchedulerEvent item = new SchedulerEvent();

					item = itemObj;
					item.setId(null);
					item.setReferrenceId(resultRequest[1]);
					item.getEventData().setStartTime(start);
					item.getEventData().setEndTime(end);
					item.getEventData().setMaximumWaitingList(appointmentSchedular.getMaximumWaitingList());
					item.getEventData().setMaxWaitingPerSlot(appointmentSchedular.getMaxWaitingPerSlot());
					item.getEventData().setConsumedWaitingPerSession(0l);
					item.getEventData().setConsumedWaitingPerSlot(0l);
					item.getEventData().setBlockWaitingSession(appointmentSchedular.getBlockWaitingSession());
					item.getEventData().setBlockWaitingSlot(appointmentSchedular.getBlockWaitingSlot());
					item.getEventData()
							.setSlotCategory(appointmentSchedular.getSlotCategory() == null ? SlotCategory.INPERSON
									: SlotCategory.valueOf(appointmentSchedular.getSlotCategory()));
					start = end;
					Calendar cal = Calendar.getInstance();
					cal.setTime(end);
					cal.add(Calendar.MINUTE, Integer.parseInt(String.valueOf(appointmentSchedular.getDuration())));
					end = cal.getTime();
					switch (appointmentSchedular.getScheduleType()) {
					case "AS":
						item.setScheduleType(CalendarScheduleType.AVAILABILITY);
						break;
					case "NS":
						item.setScheduleType(CalendarScheduleType.NON_AVAILABILITY);
						break;
					case "ADS":
						item.setScheduleType(CalendarScheduleType.ADHOC);
						break;
					default:
						break;
					}

					try {
						this.schedulerEventService.saveScheuleEvent(item, null,
								path + "/" + this.getClass().getSimpleName());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

	private void createServiceCalendar(List<SchedulerEvent> scheduleEventList, String[] resultRequest,
			ParticipantScheduleDetails appointmentSchedular, String path) {
		for (SchedulerEvent item : scheduleEventList) {
			item.setReferrenceId(resultRequest[1]);
			switch (appointmentSchedular.getScheduleType()) {
			case "AS":
				item.setScheduleType(CalendarScheduleType.AVAILABILITY);
				break;
			case "NS":
				item.setScheduleType(CalendarScheduleType.NON_AVAILABILITY);
				break;
			case "ADS":
				item.setScheduleType(CalendarScheduleType.ADHOC);
				break;
			default:
				break;
			}

			try {
				this.schedulerEventService.saveScheuleEvent(item, null, path + "/" + this.getClass().getSimpleName());
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	@Async
	private void impactedAppointments(List<Appointment> appointments, TokenPayLoad tokenPayload, String path,
			boolean isPendingReconciliation) throws ApplicationException {
		try {
			List<String> ids = new ArrayList<>();
			for (Appointment appointment : appointments) {
				ids.add(appointment.getId());
			}
//			String[] myArray = new String[ids.size()];
			String[] myArray = ids.toArray(new String[ids.size()]);
			AppointmentStatusDTO request;
			if (isPendingReconciliation) {
				request = new AppointmentStatusDTO(myArray, "PENDING-RECON", "OP-J-ST-PR",
						tokenPayload.getPreferred_username(), tokenPayload.getCoreUserId());
			} else {
				request = new AppointmentStatusDTO(myArray, "O", // Adjust as per your vice versa requirement
						"OP-J-ST-O", tokenPayload.getPreferred_username(), tokenPayload.getCoreUserId());
			}

			CustomerTransactionAttributeDTO custAttr = new CustomerTransactionAttributeDTO(true,
					tokenPayload.getCoreUserId(), new Date(), null, null, tokenPayload.getCustomerBusinessId(),
					tokenPayload.getCustomerId(), null, null);

			MessageEvent messageEvent = this.eventsMapper.convertToMessageEvent(AppUtil.convertJsonToString(request), 1,
					KafkaTopics.LT_APPOINTMENT_STATUS, custAttr, tokenPayload.getPreferred_username());
//			try {
//				this.messageEventService.saveEvent(messageEvent);
//			} catch (Exception e) {
//			}

			try {
				this.producerService.publishToKafka(1, KafkaTopics.LT_APPOINTMENT_STATUS,
						AppUtil.convertJsonToString(request), messageEvent);
			} catch (Exception e) {
//				throw new FailedException("Failed to produce kafka to LT_APPOINTMENT_STATUS !");
//				e.printStackTrace();
				MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
						AppUtil.convertJsonToString(request), 1, KafkaTopics.LT_APPOINTMENT_STATUS,
						MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
						MessageEventStatus.FAILED_ON_PUBLISH, e.getMessage());
				this.messageEventService.saveEvent(testEvent);

			}
		} catch (Exception e) {
			// TODO: handle exception
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
		}
	}

	public void publishKafkaSchedule(ParticipantScheduleDetails appointmentSchedular,
			ScheduledParticipant scheduledParticipant, TokenPayLoad tokenPayload, String path)
			throws ApplicationException {

		ScheduledParticipantMobDto scheduleObj = new ScheduledParticipantMobDto();
		List<ParticipantScheduleDetails> result = new ArrayList<>();
		result = this.getConfiguration(Long.parseLong(appointmentSchedular.getConductingSiteId()), "Consult-Calendar",
				"Practitioner", appointmentSchedular.getParticipantId(), null, tokenPayload.getCustomerId(), path);
		List<DoctorSiteMobDto> listSite = new ArrayList<>();
		if (result != null && result.size() > 0 && scheduledParticipant.getParticipantUser().getSites() != null
				&& scheduledParticipant.getParticipantUser().getSites().size() > 0) {
			for (UserSite userSite : scheduledParticipant.getParticipantUser().getSites()) {
				DoctorSiteMobDto site = new DoctorSiteMobDto();
				site.setId(String.valueOf(userSite.getId()));
				site.setName(userSite.getName());
				site.setIsDefault(userSite.getIsDefault());
				site.setAddress(userSite.getAddress());
				site.setTelecom(userSite.getTelecom() == null ? null
						: new TelecomDTO(
								userSite.getTelecom().getTelecomType() == null ? null
										: userSite.getTelecom().getTelecomType(),
								userSite.getTelecom().getTelecomCode() == null ? null
										: userSite.getTelecom().getTelecomCode(),
								userSite.getTelecom().getTelecomNumber() == null ? null
										: userSite.getTelecom().getTelecomNumber()));
				site.setSiteAvailable(userSite.getAvailability());
				site.setCustomerId(userSite.getCustomerId());
				site.setCustomerName(userSite.getCustomerName());
				site.setCustomerBusinessId(userSite.getCustomerBusinessId());
				site.setCustomerBusinessName(userSite.getCustomerBusinessName());
				List<ScheduleMobDto> listSchedule = new ArrayList<>();
				for (ParticipantScheduleDetails schDetails : result) {

					if (String.valueOf(userSite.getId()).equals(schDetails.getConductingSiteId())) {
						ScheduleMobDto sch = new ScheduleMobDto();
						sch.setScheduleFrom(schDetails.getCustomScheduleDto().getScheduleFrom());
						sch.setScheduleTo(schDetails.getCustomScheduleDto().getScheduleTo());
						sch.setScheduleFromTime(schDetails.getCustomScheduleDto().getScheduleFromTime());
						sch.setScheduleToTime(schDetails.getCustomScheduleDto().getScheduleToTime());
						sch.setDays(schDetails.getCustomScheduleDto().getDays());
						sch.setRecurrenceRule(schDetails.getCustomScheduleDto().getRecurrenceRule());
						listSchedule.add(sch);
					}
				}
				site.setSchedules(listSchedule);
				listSite.add(site);
			}

		}

		scheduleObj.setDoctorId(String.valueOf(scheduledParticipant.getParticipantUser().getCoreUserId()));
		scheduleObj
				.setDoctorName(new PersonNameMobDto(scheduledParticipant.getParticipantUser().getName().getFirstname(),
						scheduledParticipant.getParticipantUser().getName().getLastname()));
		scheduleObj.setRegistration(scheduledParticipant.getParticipantUser().getMedicalCouniclNo() == null ? null
				: new Registration(scheduledParticipant.getParticipantUser().getMedicalCouniclNo(), "", 0));
		scheduleObj.setGender(scheduledParticipant.getParticipantUser().getGender() == null ? null
				: scheduledParticipant.getParticipantUser().getGender().getDescription());
		scheduleObj.setEmail(scheduledParticipant.getParticipantUser().getEmail() == null ? null
				: scheduledParticipant.getParticipantUser().getEmail());

		Address add = new Address();
		if (scheduledParticipant.getParticipantUser().getAddress() != null) {
			add.setAddressLine1(scheduledParticipant.getParticipantUser().getAddress().getAddressLine1());
			add.setAddressLine2(scheduledParticipant.getParticipantUser().getAddress().getAddressLine1());
			add.setArea(scheduledParticipant.getParticipantUser().getAddress().getArea());
			add.setCityId(scheduledParticipant.getParticipantUser().getAddress().getCityId());
			add.setCountryId(scheduledParticipant.getParticipantUser().getAddress().getCountryId());
			add.setStateId(scheduledParticipant.getParticipantUser().getAddress().getStateId());
			add.setNationality(scheduledParticipant.getParticipantUser().getAddress().getNationality());
			add.setZipcode(scheduledParticipant.getParticipantUser().getAddress().getZipcode());

			scheduleObj.setAddress(add);
		}

		List<Specialization> specList = new ArrayList<>();
		if (scheduledParticipant.getParticipantUser().getSpecialties() != null
				&& scheduledParticipant.getParticipantUser().getSpecialties().size() > 0) {
			for (SystemMaster specialization : scheduledParticipant.getParticipantUser().getSpecialties()) {
				Specialization spec = new Specialization();
				spec.setId(String.valueOf(specialization.getId()));
				spec.setName(specialization.getNameEn());
				spec.setIdentifierCode(specialization.getIdentifierCode());
				specList.add(spec);
			}

		}

		List<WorkExperienceDTO> listExp = new ArrayList<>();
		if (scheduledParticipant.getParticipantUser().getWorkExperience() != null
				&& scheduledParticipant.getParticipantUser().getWorkExperience().size() > 0) {
			listExp.addAll(scheduledParticipant.getParticipantUser().getWorkExperience());
		}

		List<Qualification> listQuali = new ArrayList<>();
		if (scheduledParticipant.getParticipantUser().getUserQualification() != null
				&& scheduledParticipant.getParticipantUser().getUserQualification().size() > 0) {

			for (QualificationDTO qualification : scheduledParticipant.getParticipantUser().getUserQualification()) {
				Qualification quli = new Qualification();
				quli.setEducationLevel(qualification.getEducationLevel());
				quli.setPassingYear(qualification.getDate().toString());
				quli.setDegree(qualification.getCourse());
				quli.setUniversity("");
				listQuali.add(quli);
			}
		}

		String[] language = null;
		if (scheduledParticipant.getParticipantUser().getKnownLanguage() != null
				&& scheduledParticipant.getParticipantUser().getKnownLanguage().size() > 0) {
			language = new String[scheduledParticipant.getParticipantUser().getKnownLanguage().size()];

			for (int i = 0; i < scheduledParticipant.getParticipantUser().getKnownLanguage().size(); i++) {
				language[i] = scheduledParticipant.getParticipantUser().getKnownLanguage().get(i).getName();
			}
		}

		scheduleObj.setSpecialization(specList);
		scheduleObj.setYearsOfExpirence(listExp);
		scheduleObj.setPreference(scheduledParticipant.getParticipantUser().getPreference());
		scheduleObj.setDesignation(scheduledParticipant.getParticipantUser().getDesignation());
		scheduleObj.setProfessionalStatement(
				scheduledParticipant.getParticipantUser().getProfessionalStatement() == null ? null
						: scheduledParticipant.getParticipantUser().getProfessionalStatement());
		scheduleObj.setQualifications(listQuali);
		scheduleObj.setKnownLanguages(language);
		scheduleObj.setProfilePhoto(scheduledParticipant.getParticipantUser().getProfilePhoto() == null ? null
				: scheduledParticipant.getParticipantUser().getProfilePhoto());
		scheduleObj.setSites(listSite);
		if (scheduledParticipant.getParticipantUser().getNationality() != null) {
			scheduleObj.setNationality(scheduledParticipant.getParticipantUser().getNationality()[0]);
		} else {
			scheduleObj.setNationality(null);
		}

		if (scheduledParticipant.getParticipantUser().getTelecom() != null) {
			scheduleObj.setTelecom(scheduledParticipant.getParticipantUser().getTelecom());
		}

		KafkaTransactionBase trans = new KafkaTransactionBase();
		trans.setCreatedById(appointmentSchedular.getCreatedById());
		trans.setCustomerBusinessId(appointmentSchedular.getCustomerBusinessId());
		trans.setCustomerId(appointmentSchedular.getCustomerId());
		trans.setSiteId(appointmentSchedular.getSiteId());
		scheduleObj.setTransactionBase(trans);

		try {
			if (tokenPayload == null) {
				tokenPayload = new TokenPayLoad();
				tokenPayload.setCustomerId(appointmentSchedular.getCustomerId());
				tokenPayload.setCustomerBusinessId(appointmentSchedular.getCustomerBusinessId());
				tokenPayload.setName(appointmentSchedular.getCreatedBy());
			}

			CustomerTransactionAttributeDTO custAttr = new CustomerTransactionAttributeDTO(true, null, new Date(), null,
					null, trans.getCustomerBusinessId(), trans.getCustomerId(), trans.getSiteId(), null);

			MessageEvent messageEvent = this.eventsMapper.convertToMessageEvent(
					AppUtil.convertJsonToString(scheduleObj), KafkaTopic.SCHEDULED_PARTICIPANTS.value,
					KafkaTopics.SCHEDULED_PARTICIPANTS.toString(), custAttr, appointmentSchedular.getCreatedBy());

//			this.messageEventService.saveEvent(messageEvent);
			this.producerService.publishToKafka(1, KafkaTopics.SCHEDULED_PARTICIPANTS.toString(),
					AppUtils.convertJsonToString(scheduleObj), messageEvent);
		} catch (Exception e1) {
//			e1.printStackTrace();
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
					AppUtils.convertJsonToString(scheduleObj), 1, KafkaTopics.SCHEDULED_PARTICIPANTS,
					MessageRequestStatus.PENDING, path + "/" + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_PUBLISH, e1.getMessage());
			this.messageEventService.saveEvent(testEvent);

		}

	}

	@Override
	public List<ParticipantScheduleDetails> getConfiguration(Long siteId, String calendarType, String participantType,
			String participantId, List<String> scheduleType, Long customerId, String path) {
		Query query = new Query();

		query.addCriteria(Criteria.where("isActive").is(true));

		if (customerId != null) {
			query.addCriteria(Criteria.where("customerId").is(customerId));
		}

		if (scheduleType != null && scheduleType.size() > 0) {

			query.addCriteria(Criteria.where("scheduleType").in(scheduleType));
		}

		query.addCriteria(Criteria.where("conductingSiteId").is(String.valueOf(siteId)));

		query.addCriteria(Criteria.where("calendarType.identifierCode").in(calendarType));

		if (!AppUtils.isNullString(participantType)) {
			query.addCriteria(Criteria.where("participantType.identifierCode").is(participantType));
		}

		if (!AppUtils.isNullString(participantId)) {
			query.addCriteria(Criteria.where("participantId").is(participantId));

		}

		query.fields().include("slotType", "duration", "participantId", "customScheduleDto", "scheduleConfig", "reason",
				"participantName", "maxWaitingPerSlot", "maximumWaitingList");

		List<ParticipantScheduleDetails> result = this.mongoTemplate.find(query, ParticipantScheduleDetails.class);

		return result;
	}

	@Override
	public List<ScheduledParticipantGetDTO> getScheduledParticipant(Long siteId, Long conductingSiteId,
			String participantType, Long participantId, String speciality, String gender, String nationality,
			String qualification, String[] language, String fromDate, String toDate, String physicianFirstName,
			String physicianMiddleName, String physicianLastName, Long isTop, String type, Long customerId, String path,
			Long departmentId) throws ApplicationException {

		try {
			Query query = new Query();

			query.addCriteria(Criteria.where("active").is(true));

			if (customerId != null) {
				query.addCriteria(Criteria.where("customerId").is(customerId));
			}

			if (siteId != null) {
				query.addCriteria(Criteria.where("siteId").is(siteId));
			}
			if (conductingSiteId != null) {
				query.addCriteria(Criteria.where("conductingSiteId").is(conductingSiteId));
			}
			if (!AppUtils.isNullString(participantType)) {
				query.addCriteria(Criteria.where("participantType.identifierCode").is(participantType));
			}

			if (!AppUtils.isNullString(String.valueOf(participantId))) {
				query.addCriteria(Criteria.where("participantId").is(participantId));
			}

			if (!AppUtils.isNullString(String.valueOf(departmentId))) {
				query.addCriteria(
						Criteria.where("participantUser.department").elemMatch(Criteria.where("_id").is(departmentId)));
			}

			if (participantType.equals("Practitioner")) {
				if (!AppUtils.isNullString(physicianFirstName)) {
					query.addCriteria(Criteria.where("participantUser.name.firstname").regex(physicianFirstName, "i"));

				}

				if (!AppUtils.isNullString(speciality)) {

					query.addCriteria(Criteria.where("participantUser.specialties")
							.elemMatch(Criteria.where("identifierCode").is(speciality)));

				}

				if (!AppUtils.isNullString(gender)) {
					query.addCriteria(Criteria.where("participantUser.gender.identifierCode").is(gender));
				}

				if (!AppUtils.isNullString(nationality)) {
					query.addCriteria(Criteria.where("participantUser.address.nationality").is(nationality));
				}

				if (!AppUtils.isNullString(qualification)) {
					query.addCriteria(
							Criteria.where("participantUser.userQualification.qualification").is(qualification));
				}

				if (language != null && language.length > 0) {
					query.addCriteria(Criteria.where("participantUser.appLanguage.identifierCode").in(language));
				}

				if (!AppUtils.isNullString(String.valueOf(isTop))) {
					query.with(Sort.by(Sort.Direction.DESC, "scheduleCount"));
					query.limit(20);
				}

				if (!AppUtils.isNullString(type)) {
					query.addCriteria(Criteria.where("calendarType.identifierCode").is(type));
				}
			}

			query.fields().include("slotType", "duration", "participantId", "customScheduleDto", "scheduleConfig",
					"participantUser", "isLogin");

			List<ScheduledParticipant> result = this.mongoTemplate.find(query, ScheduledParticipant.class);

			List<ScheduledParticipantGetDTO> response = result.stream()
					.map(scheduleMapper::convertToScheduledParticipantGetDto).collect(Collectors.toList());

			return response;
		} catch (Exception e) {
			// TODO: handle exception
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
			return null;
		}

	}

	@Override
	public ParticipantScheduleGetDto deleteParticipantSchedule(String scheduleId, TokenPayLoad tokenPayload,
			String path, Boolean isOverride) throws ApplicationException {
		try {
			ParticipantScheduleDetails result = this.appointmentSchedularRepository.findById(scheduleId).get();

			if (result != null) {
				result.setIsActive(false);
				result.setUpdatedBy(tokenPayload.getName());
				result.setUpdatedById(tokenPayload.getCoreUserId());
				result = this.appointmentSchedularRepository.save(result);

				List<SchedulerEvent> ress = this.schedulerEventService.getScheduleEventEntityByReference(scheduleId);
				ress.get(0).getCustomerTransaction().setActive(false);

				this.schedulerEventRepository.saveAll(ress);

				if (isOverride) {
					List<Appointment> appt = this.appointmentRepository.getNonAvab(
							Long.valueOf(result.getConductingSiteId()), result.getCustomScheduleDto().getScheduleFrom(),
							result.getCustomScheduleDto().getScheduleTo(),
							result.getParticipantId() == null ? null : result.getParticipantId(), null,
							result.getParticipantType() == null ? null
									: String.valueOf(result.getParticipantType().getIdentifierCode()),
							null, "PENDING-RECON", null);
					appt.forEach(item -> {
						item.setAppointmentStatus(new SystemMasterDTO(null, "Open", "Open", "O"));

						try {
							List<SchedulerEvent> scheEvent = this.schedulerEventService
									.getScheduleEventByAppointmentId(item.getId());
							if (scheEvent != null && scheEvent.size() > 0) {
								for (SchedulerEvent appEvent : scheEvent) {
									if (appEvent.getEventData() != null) {
										appEvent.getEventData().setEventStatus(ScheduleEventStatus.OPEN);
										appEvent.getEventData().setSubject("");
										appEvent.getEventData().setPatientId("");
										appEvent.getEventData().setConductMode(null);
										appEvent.getEventData().setAppointmentId("");
										appEvent.getEventData().setLocationRoomName(null);
									}

								}
								this.schedulerEventRepository.saveAll(scheEvent);
							}
						} catch (ApplicationException e) {

							e.printStackTrace();
						}

					});
					if (appt.size() > 0) {
						this.appointmentRepo.saveAll(appt);
						this.impactedAppointments(appt, tokenPayload, path, false);
					}
				}

			} else {
				throw new FailedException("ParticipantScheduleDetails with ID " + scheduleId + " not found.");
			}

			ParticipantScheduleGetDto response = this.scheduleMapper.convertToParticipantScheduledGetDto(result);
			return response;
		} catch (Exception e) {
			// TODO: handle exception
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
			return null;
		}
	}

	@Override
	public List<OnlineDoctors> getAllDoctors(Long customerBusinessId, String path) throws ApplicationException {

		try {
			if (customerBusinessId == null) {
				throw new Exception("Customer Business ID cannot be null");
			}

			Query query = new Query();
			query.addCriteria(Criteria.where("active").is(true));
			query.addCriteria(Criteria.where("customerBusinessId").is(customerBusinessId));
			query.addCriteria(Criteria.where("participantUser").ne(null));

			List<ScheduledParticipant> scheduledParticipants = mongoTemplate.find(query, ScheduledParticipant.class);

			if (scheduledParticipants.isEmpty()) {
				throw new Exception("No scheduled participants found for Customer Business ID: " + customerBusinessId);
			}

			List<User> users = scheduledParticipants.stream().map(ScheduledParticipant::getParticipantUser)
					.collect(Collectors.toList());

			return users.stream().map(doctorMapper::mapToOnlineDoctors).collect(Collectors.toList());
		} catch (Exception e) {
			log.error("Unexpected error occurred while fetching doctors: {}", e.getMessage(), e);
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
			throw new RuntimeException("An unexpected error occurred while fetching doctors", e);
		}

	}

	@Override
	public List<ScheduledParticipantGetDTO> getParticipantsByCalendarType(String calendarType, String participantType,
			Long siteId, Long customerBusinessId, Long customerId, String path) throws ApplicationException {

		try {
			List<ScheduledParticipant> result = new ArrayList<>();
			Query query = new Query();
			query.addCriteria(Criteria.where("active").is(true));
			if (customerId != null) {
				query.addCriteria(Criteria.where("customerId").is(customerId));
			}

			if (calendarType != null && calendarType != "") {
				query.addCriteria(Criteria.where("calendarType.identifierCode").is(calendarType));
			}

			if (participantType != null && participantType != "") {
				query.addCriteria(Criteria.where("participantType.identifierCode").is(participantType));
			}

			if (siteId != null) {
				query.addCriteria(Criteria.where("conductingSiteId").is(siteId));
			}

			query.addCriteria(Criteria.where("customerBusinessId").is(customerBusinessId));

//			query.fields().include("equipment", "location", "participantUser");

			result = mongoTemplate.find(query, ScheduledParticipant.class);

			List<ScheduledParticipantGetDTO> response = result.stream()
					.map(scheduleMapper::convertToScheduledParticipantGetDto).collect(Collectors.toList());

			return response;
		} catch (Exception e) {
			// TODO: handle exception
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
			return null;
		}

	}

	@Override
	public List<DoctorSlots> getAllSlotsForDate(Long date, String path) throws ApplicationException {
		try {
			List<DoctorSlots> doctorSlotsList = new ArrayList<>();

			Date fromDate = new Date(date);
			Calendar calendarFrom = Calendar.getInstance();
			calendarFrom.setTime(fromDate);
			calendarFrom.add(Calendar.HOUR_OF_DAY, 0);
			calendarFrom.add(Calendar.MINUTE, 0);
			calendarFrom.add(Calendar.SECOND, 30);

			Calendar calendarTo = Calendar.getInstance();
			calendarTo.setTime(fromDate);
			calendarTo.add(Calendar.HOUR_OF_DAY, 23);
			calendarTo.add(Calendar.MINUTE, 59);
			calendarTo.add(Calendar.SECOND, 30);

			// Fetch all participant schedules for the date
			Query query = new Query();
			query.addCriteria(Criteria.where("isActive").is(true));
			query.addCriteria(Criteria.where("calendarType.identifierCode").is("Consult-Calendar"));
			query.addCriteria(Criteria.where("customScheduleDto.scheduleFrom").lte(calendarTo.getTimeInMillis()));
			query.addCriteria(Criteria.where("customScheduleDto.scheduleTo").gte(calendarFrom.getTimeInMillis()));

			List<ParticipantScheduleDetails> result = mongoTemplate.find(query, ParticipantScheduleDetails.class);

			Set<String> processedDoctors = new HashSet<>();
			for (ParticipantScheduleDetails participantScheduleDetails : result) {
				String doctorId = participantScheduleDetails.getParticipantId();
				String siteId = participantScheduleDetails.getConductingSiteId();

				if (processedDoctors.contains(doctorId))
					continue;

				List<Appointment> getApptSlot = this.appointmentRepository.getDoctorAppointments(doctorId,
						String.valueOf(calendarFrom.getTimeInMillis()), String.valueOf(calendarTo.getTimeInMillis()),
						null);

				DoctorSlots doctorSlots = generateDoctorSlotsForSchedule(participantScheduleDetails, date, getApptSlot);
				doctorSlotsList.add(doctorSlots);
				processedDoctors.add(doctorId);
			}

			return doctorSlotsList;
		} catch (Exception e) {
			// TODO: handle exception
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
			return null;
		}
	}

	private DoctorSlots generateDoctorSlotsForSchedule(ParticipantScheduleDetails participantScheduleDetails, Long date,
			List<Appointment> getApptSlot) {
		String doctorId = participantScheduleDetails.getParticipantId();
		String dow = AppUtil.getDayOfWeek(new Date(date));
		DoctorSlots doctorSlots = new DoctorSlots(doctorId, participantScheduleDetails.getParticipantName(),
				participantScheduleDetails.getParticipantType().getDescription(), AppUtil.getLocalDate(date),
				participantScheduleDetails.getConductingSiteId(), 0, 0, null);

		List<Slot> slots = new ArrayList<>();
		if (!participantScheduleDetails.getScheduleType().equals("NS")) {
			boolean isExistDOW = Arrays.asList(participantScheduleDetails.getCustomScheduleDto().getDays())
					.contains(dow);

			if (isExistDOW) {
				long consultingDuration = AppUtil.getTimeDifferenceInMinutes(
						participantScheduleDetails.getCustomScheduleDto().getScheduleFromTime(),
						participantScheduleDetails.getCustomScheduleDto().getScheduleToTime());

				int noOfSlots = (int) (consultingDuration / participantScheduleDetails.getDuration());
				Calendar cal = AppUtil
						.getCurrentTime(participantScheduleDetails.getCustomScheduleDto().getScheduleFromTime());
				Calendar cal2 = AppUtil
						.getCurrentTime(participantScheduleDetails.getCustomScheduleDto().getScheduleFromTime());

				for (int i = 0; i < noOfSlots; i++) {
					Slot slot = new Slot(UUID.randomUUID().toString(), AppUtil.getLocalTime(cal, 0),
							AppUtil.getLocalTime(cal, (int) (participantScheduleDetails.getDuration() - 0)),
							SlotStatus.AVAILABLE, SlotReservation.ONLINE, 0, 0,
							getLocalDateTime(new Date(date), cal2, 0), getLocalDateTime(new Date(date), cal2,
									(int) (participantScheduleDetails.getDuration() - 0)));

					slots.add(slot);
				}

				for (Slot slot : slots) {
					SimpleDateFormat currSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
					Date currDate = new Date();
					Date slotDate = new Date();
					Date slotEndDate = new Date();

					try {
						currDate = currSdf.parse(currSdf.format(currDate));
						slotDate = currSdf.parse(currSdf.format(slot.getStartDate()));
						slotEndDate = currSdf.parse(currSdf.format(slot.getEndDate()));
					} catch (ParseException e) {
					}

					if (currDate.getTime() > slotDate.getTime()) {
						slot.setStatus(SlotStatus.ELAPSED);
					} else {
						slot.setStatus(SlotStatus.AVAILABLE);
					}

					for (Appointment appointment : getApptSlot) {
						Date apptDate = new Date();
						try {
							apptDate = currSdf.parse(currSdf.format(appointment.getStartDate()));
						} catch (ParseException e) {
						}
						if (apptDate.getTime() >= slotDate.getTime() && apptDate.getTime() < slotEndDate.getTime()) {
							slot.setStatus(SlotStatus.OCCUPIED);
						}
					}
				}

				doctorSlots.setSlots(slots);
			}
		}

		if (participantScheduleDetails.getScheduleType().equals("NS")) {
			if (doctorSlots != null && doctorSlots.getSlots() != null && doctorSlots.getSlots().size() != 0) {
				for (Slot nonAvailSlot : doctorSlots.getSlots()) {
					SimpleDateFormat currSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
					Date nonFromDate = new Date();
					Date nonToDate = new Date();
					Date slotFromDate = new Date();
					Date slotEndDate = new Date();

					try {
						nonFromDate = currSdf.parse(
								currSdf.format(participantScheduleDetails.getCustomScheduleDto().getScheduleFrom()));
						nonToDate = currSdf.parse(
								currSdf.format(participantScheduleDetails.getCustomScheduleDto().getScheduleTo()));
						slotFromDate = currSdf.parse(currSdf.format(nonAvailSlot.getStartDate()));
						slotEndDate = currSdf.parse(currSdf.format(nonAvailSlot.getEndDate()));
					} catch (ParseException e) {
					}

					if (slotFromDate.getTime() >= nonFromDate.getTime()
							&& slotEndDate.getTime() <= nonToDate.getTime()) {
						nonAvailSlot.setStatus(SlotStatus.NOT_AVAILABLE);
					}
				}
			}
		}

		return doctorSlots;
	}

	Date getLocalDateTime(Date date, Calendar calendar, int duration) {

		calendar.add(Calendar.MINUTE, duration);
		Instant instant = calendar.toInstant();
		ZoneId zoneId = TimeZone.getDefault().toZoneId();
		LocalTime localTime = LocalTime.ofInstant(instant, zoneId);

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date);
		cal1.set(Calendar.HOUR_OF_DAY, localTime.getHour());
		cal1.set(Calendar.MINUTE, localTime.getMinute());
		cal1.set(Calendar.SECOND, localTime.getSecond());

		return cal1.getTime();
	}

	@Override
	public PagenationParticipantScheduleDetailsDto getConfigurationList(TokenPayLoad tokenPayLoad, Long siteId,
			String calendarType, String participantType, String participantId, Long startDate, Long endDate,
			String scheduleType, String speciality, String path,Integer pageSize, Integer pageNo, String sortBy) {

		Query query = new Query();
		List<ScheduledParticipant> scheduledParticipants;
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		if (!AppUtil.isNullString(speciality)) {
			scheduledParticipants = this.getScheduledParticipantsBySpeciality(tokenPayLoad, speciality,pageSize,pageNo,sortBy);
			if (scheduledParticipants != null && scheduledParticipants.size() > 0) {
				query.addCriteria(Criteria.where("scheduledParticipant").in(scheduledParticipants));
			}

		}

		Date dt1 = new Date(startDate);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(dt1);
		c1.add(Calendar.DATE, 0);
		dt1 = c1.getTime();

		Date dt = new Date(endDate);

		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.DATE, 1);
		dt = c.getTime();

		query.addCriteria(Criteria.where("isActive").is(true));

		query.addCriteria(Criteria.where("siteId").is(siteId));

		query.addCriteria(Criteria.where("calendarType.identifierCode").in(calendarType));
		if (startDate != null && endDate != null) {
			query.addCriteria(Criteria.where("customScheduleDto.scheduleFrom").gte((startDate)).lte((endDate)));
		}

		query.addCriteria(Criteria.where("participantType.identifierCode").is(participantType));

		if (!AppUtil.isNullString(participantId)) {
			query.addCriteria(Criteria.where("participantId").is(participantId));

		}

		if (scheduleType != null && !AppUtils.isNullString(scheduleType)) {
			query.addCriteria(Criteria.where("scheduleType").is(scheduleType));

		}

		List<ParticipantScheduleDetails> result = this.mongoTemplate.find(query, ParticipantScheduleDetails.class);
		PagenationParticipantScheduleDetailsDto pageuserDto = new PagenationParticipantScheduleDetailsDto();
		long totalCount = result.size();
		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), result.size());

		List<ParticipantScheduleDetails> pagedList;
		if (start >= result.size()) {
			pagedList = new ArrayList<>();
		} else {
			pagedList = result.subList(start, end);
		}

		Page<ParticipantScheduleDetails> userPage = new PageImpl<>(pagedList, pageable, totalCount);

		PageDTO pageDto = new PageDTO();
		pageDto.setCurrentPageNo(pageNo);
		pageDto.setCurrentPageSize(pageSize);
		pageDto.setTotal(totalCount);
		pageDto.setTotalPages((int) Math.ceil((double) totalCount / pageSize));

		pageuserDto.setUserDTO(userPage);
		pageuserDto.setPageDTO(pageDto);

		return pageuserDto;
	}

	public ScheduledParticipant updateUserProfileStatus(UserUpdateProfileDTO userUpdateProfileDTO)
			throws ApplicationException {
		ScheduledParticipant scheduledParticipant = null;
		ScheduledParticipant updatedParticipant = null;
		
		if (userUpdateProfileDTO != null && userUpdateProfileDTO.getCoreUserId() != null) {
			Query query = new Query();

			query.addCriteria(Criteria.where("participantUser.coreUserId").is(userUpdateProfileDTO.getCoreUserId()));

			List<ScheduledParticipant> result = null;

			try {
				result = this.mongoTemplate.find(query, ScheduledParticipant.class);
			} catch (Exception e) {
			}

			if (result != null && result.size() > 0) {
				scheduledParticipant = result.get(0);
				scheduledParticipant = this.scheduleMapper.getScheduledParticipant(scheduledParticipant,
						userUpdateProfileDTO);
				try {
					scheduledParticipant.setId(result.get(0).getId());

					updatedParticipant = this.scheduledParticipantRepository.save(scheduledParticipant);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return updatedParticipant;
	}

	public void updateImageForUser(Long coreUserId, String profilePath) {

		List<ScheduledParticipant> scheduledParticipant = null;

		try {
			scheduledParticipant = scheduledParticipantRepository.findByParticipantId(coreUserId);
			if (scheduledParticipant != null && scheduledParticipant.size() > 0) {
				for (int i = 0; i < scheduledParticipant.size(); i++) {
					scheduledParticipant.get(i).getParticipantUser().setProfilePhoto(profilePath);
				}
				scheduledParticipantRepository.saveAll(scheduledParticipant);
			}
		} catch (Exception e) {
		}

	}

	@Override
	public List<SystemMasterDTO> getParticipantSpecialities(Long customerBusinessId, String path)
			throws ApplicationException {
		try {
			List<ScheduledParticipant> scheduledParticipants = null;
			try {
				scheduledParticipants = scheduledParticipantRepository.findByCustomerBusinessId(customerBusinessId);
			} catch (Exception e) {
				throw new NotFoundException("Participants Not Found.");
			}

			List<SystemMasterDTO> unfilteredResponse = new ArrayList<>();

			scheduledParticipants.stream().forEach(sp -> {
				try {
					if (sp.getParticipantUser() != null && sp.getParticipantUser().getSpecialties() != null) {
						sp.getParticipantUser().getSpecialties().forEach(spec -> {
							SystemMasterDTO speciality = new SystemMasterDTO();
							speciality.setDescription(spec.getDescription());
							speciality.setId(spec.getId());
							speciality.setIdentifierCode(spec.getIdentifierCode());
							speciality.setNameEn(spec.getNameEn());

							unfilteredResponse.add(speciality);
						});
					}
				} catch (Exception e) {
				}

			});

//			List<SystemMasterDTO> filteredResponse = unfilteredResponse.stream()
//					.collect(Collectors.groupingBy(SystemMasterDTO::getId,
//							Collectors.maxBy(Comparator.comparingLong(SystemMasterDTO::getId))))
//					.values().stream().map(opt -> opt.orElse(null)).collect(Collectors.toList());
			List<SystemMasterDTO> filteredList = unfilteredResponse.stream()
					.filter(systemDTO -> systemDTO.getId() != null).collect(Collectors.toMap(SystemMasterDTO::getId,
							Function.identity(), (existing, replacement) -> existing))
					.values().stream().collect(Collectors.toList());
			return filteredList;
		} catch (Exception e) {
			// TODO: handle exception
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
			return null;
		}
	}

	@Override
	public void updateParticipantScheduleCount(Long siteId, Long participantId) throws ApplicationException {

		ScheduledParticipant scheduledParticipant = this.scheduledParticipantRepository
				.findByParticipantIdAndConductingSiteId(participantId, siteId);
		scheduledParticipant.setScheduleCount(scheduledParticipant.getScheduleCount() + 1);
	}

	@Override
	public List<ScheduledParticipantGetDTO> getRecentParticipantList(Long siteId, String physicianName, String type,
			Long customerId, String path, Long departmentId) throws ApplicationException {

		try {
			List<Long> paticipantIds = this.findRecentAppointmentParticipantId().stream().map(Long::valueOf)
					.collect(Collectors.toList());

			Query query = new Query();
			query.addCriteria(Criteria.where("active").is(true));
			if (customerId != null) {
				query.addCriteria(Criteria.where("customerId").is(customerId));
			}
			if (paticipantIds.size() > 0)

			{
				query.addCriteria(Criteria.where("paticipantId").is(paticipantIds));
			}
			query.addCriteria(Criteria.where("conductingSiteId").is(siteId));
			if (!AppUtils.isNullString(physicianName)) {
				query.addCriteria(Criteria.where("participantUser.name.firstname").regex(physicianName, "i"));

			}

			if (departmentId != null && departmentId != 0) {
				query.addCriteria(
						Criteria.where("participantUser.department").elemMatch(Criteria.where("_id").is(departmentId)));

			}

			if (!AppUtils.isNullString(type)) {
				query.addCriteria(Criteria.where("calendarType.identifierCode").is(type));
			}

			query.fields().include("participantUser", "consultingLocation", "isLogin", "scheduleParticipantUser",
					"participantId", "siteId");

			List<ScheduledParticipant> result = this.mongoTemplate.find(query, ScheduledParticipant.class);

			List<ScheduledParticipantGetDTO> scheduledParticipantGetDTOs = result.stream()
					.map(scheduleMapper::convertToScheduledParticipantGetDto).collect(Collectors.toList());

			return scheduledParticipantGetDTOs;
		} catch (Exception e) {
			// TODO: handle exception
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
			return null;
		}

	}

	@Override
	public List<ScheduledParticipantGetDTO> getMyPhysicianList(Long siteId, String patientId, String path)
			throws ApplicationException {
		try {
			List<Long> paticipantIds = this.findAppointmentIdsForParticipant(patientId).stream().map(Long::valueOf)
					.collect(Collectors.toList());

			Query query = new Query();

			query.addCriteria(Criteria.where("active").is(true));

			query.addCriteria(Criteria.where("participantId").in(paticipantIds));

			query.addCriteria(Criteria.where("conductingSiteId").is(siteId));

			query.fields().include("participantId", "participantUser");

			List<ScheduledParticipant> result = this.mongoTemplate.find(query, ScheduledParticipant.class);

			List<ScheduledParticipantGetDTO> response = result.stream()
					.map(scheduleMapper::convertToScheduledParticipantGetDto).collect(Collectors.toList());

			return response;
		} catch (Exception e) {
			// TODO: handle exception
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
			return null;
		}

	}

	@Override
	public List<ScheduledParticipant> getScheduledParticipantsBySpeciality(TokenPayLoad tokenPayLoad,
			String speciality,Integer pageSize, Integer pageNo, String sortBy) {
		Query query = new Query();
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		query.addCriteria(Criteria.where("active").is(true));
		if (tokenPayLoad.getCustomerBusinessId() != null) {
			query.addCriteria(Criteria.where("customerBusinessId").is(tokenPayLoad.getCustomerBusinessId()));
		}

		if (!AppUtils.isNullString(speciality)) {

			query.addCriteria(Criteria.where("participantUser.specialties")
					.elemMatch(Criteria.where("identifierCode").is(speciality)));

		}

		List<ScheduledParticipant> result = this.mongoTemplate.find(query, ScheduledParticipant.class);
		
		return result;
	}

	public List<String> findRecentAppointmentParticipantId() {
		Query query = new Query();
		LocalDate today = LocalDate.now();
		query.limit(20);
		query.with(Sort.by(Sort.Direction.DESC, "appointmentStartDate"));

		query.addCriteria(Criteria.where("appointmentStartDate").exists(true));
		query.addCriteria(Criteria.where("createdOn").gte(today.plusDays(-30)).lte(today.plusDays(1)));

		return mongoTemplate.query(Appointment.class).distinct("particpantCalendar.participantId").matching(query)
				.as(String.class).all();

	}

//	public List<String> findMyPhysician(String patientId) {
//		Query query = new Query();
//
//		query.limit(20);
//		query.with(Sort.by(Sort.Direction.DESC, "appointmentStartDate"));
//
//		query.addCriteria(Criteria.where("appointmentStartDate").exists(true));
//
//		Criteria criteria = new Criteria().andOperator(
//                Criteria.where("particpantCalendar.appointmentParticipantType.identifierCode").is("Patient"),  
//                Criteria.where("particpantCalendar.participantId").is(patientId));
//		query.addCriteria(criteria);
//		return mongoTemplate.query(Appointment.class).distinct("particpantCalendar.participantId").matching(query)
//				.as(String.class).all();
//
//	}
	public List<String> findAppointmentIdsForParticipant(String patientId) {
		Query query = new Query();
		Criteria criteria = new Criteria().andOperator(
				Criteria.where("particpantCalendar.appointmentParticipantType.identifierCode").is("Patient"),
				Criteria.where("particpantCalendar.participantId").is(patientId));
		query.addCriteria(criteria);
		query.addCriteria(Criteria.where("startDate").exists(true));
//	    List<Appointment> appointments = mongoTemplate.find(query, Appointment.class);
		return mongoTemplate.query(Appointment.class).distinct("particpantCalendar.participantId").matching(query)
				.as(String.class).all();
//
	}

	@Override
	public ParticipantScheduleDetailsGetDTO getParticipantDate(Long siteId, String calendarType, String participantType,
			String participantId, String scheduleType, Long startDate, Long endDate, Long customerId, int page,
			int size, String path) throws ApplicationException {
		try {
			Pageable pageable = PageRequest.of(page, size);

			Query query = new Query();
			query.addCriteria(Criteria.where("isActive").is(true));

			if (customerId != null) {
				query.addCriteria(Criteria.where("customerId").is(customerId));
			}

			if (!AppUtil.isNullString(scheduleType)) {
				query.addCriteria(Criteria.where("scheduleType").is(scheduleType));
			}

			if (siteId > 0) {
				query.addCriteria(Criteria.where("conductingSiteId").is(String.valueOf(siteId)));
			}

			query.addCriteria(Criteria.where("calendarType.identifierCode").in(calendarType));
			if (startDate != null && endDate != null) {
				query.addCriteria(Criteria.where("customScheduleDto.scheduleFrom").gte((startDate)).lte((endDate)));
			}
			if (!AppUtils.isNullString(participantType)) {
				query.addCriteria(Criteria.where("participantType.identifierCode").is(participantType));
			}

			if (!AppUtils.isNullString(participantId)) {
				query.addCriteria(Criteria.where("participantId").is(participantId));

			}

			long totalCount = mongoOperations.count(query, ParticipantScheduleDetails.class);
			query.with(pageable);
			PageDTO pageDto = new PageDTO();

			pageDto.setCurrentPageNo(page);
			pageDto.setCurrentPageSize(size);
			pageDto.setTotal(totalCount);
			pageDto.setTotalPages(page);

			query.fields().include("participantType", "participantName", "customScheduleDto", "duration", "slotType",
					"slotCategory", "participantId", "conductingSiteId", "maximumWaitingList", "maxWaitingPerSlot",

					"conductingSiteName", "visitType", "speciality");

			List<ParticipantScheduleDetails> list = this.mongoTemplate.find(query, ParticipantScheduleDetails.class);

			Page<ParticipantScheduleDetails> userPage = PageableExecutionUtils.getPage(list, pageable,
					() -> mongoTemplate.count(query, ParticipantScheduleDetails.class));

			ParticipantScheduleDetailsGetDTO response = new ParticipantScheduleDetailsGetDTO();

			response.setData(userPage);
			response.setPage(pageDto);

			return response;
		} catch (Exception e) {
			// TODO: handle exception
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
			return null;
		}
	}

	@Override
	public List<ScheduledParticipantGetDTO> getPhysicianParticipateId(Long siteId, Long participantId, String path)
			throws ApplicationException {

		try {
			Query query = new Query();

			query.addCriteria(Criteria.where("participantId").is(participantId));

			query.addCriteria(Criteria.where("conductingSiteId").is(siteId));

			query.fields().include("participantUser", "scheduleParticipantUser", "participantId", "siteId", "isLogin");

			List<ScheduledParticipant> result = this.mongoTemplate.find(query, ScheduledParticipant.class);

			List<ScheduledParticipantGetDTO> response = result.stream()
					.map(scheduleMapper::convertToScheduledParticipantGetDto).collect(Collectors.toList());

			return response;
		} catch (Exception e) {
			// TODO: handle exception
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
			return null;
		}
	}

	@Override
	public List<DoctorListDto> getDoctorsForAppointmet(Long date, Long siteId, String path)
			throws ApplicationException {
		try {
			List<ParticipantScheduleDetails> doctorcheduleDetails = this.getParticipantScheduleDetails(siteId,
					"Consult-Calendar", "Practitioner", date, null);

			List<DoctorListDto> finalList = new ArrayList<>();
			if (doctorcheduleDetails.size() > 0) {
				List<ParticipantScheduleDetails> nonAvailabilityList = doctorcheduleDetails.stream()
						.filter(psd -> "NS".equals(psd.getScheduleType())).collect(Collectors.toList());

				List<ParticipantScheduleDetails> availabilityList = doctorcheduleDetails.stream()
						.filter(psd -> !"NS".equals(psd.getScheduleType())).collect(Collectors.toList());

				finalList = this.appointmentMapper.getDoctorList(availabilityList, nonAvailabilityList,
						AppUtil.convertEpochToDate(date));
			}

			return finalList;
		} catch (Exception e) {
			// TODO: handle exception
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
			return null;
		}
	}

	@Override
	public List<ParticipantScheduleDetails> getParticipantScheduleDetails(Long siteId, String calendarType,
			String participantType, Long date, String scheduleType) throws ApplicationException {
		List<ParticipantScheduleDetails> result = null;

		Date fromDate = new Date(date);
		Calendar calendarFrom = Calendar.getInstance();
		calendarFrom.setTime(fromDate);
		calendarFrom.add(Calendar.HOUR_OF_DAY, 0);
		calendarFrom.add(Calendar.MINUTE, 0);
		calendarFrom.add(Calendar.SECOND, 30);
		Calendar calendarTo = Calendar.getInstance();
		calendarTo.setTime(fromDate);
		calendarTo.add(Calendar.HOUR_OF_DAY, 23);
		calendarTo.add(Calendar.MINUTE, 59);
		calendarTo.add(Calendar.SECOND, 30);

		Query query = new Query();
		query.addCriteria(Criteria.where("conductingSiteId").is(String.valueOf(siteId)));
		query.addCriteria(Criteria.where("isActive").is(true));
		query.addCriteria(Criteria.where("participantId").is("16256"));
		query.addCriteria(Criteria.where("calendarType.identifierCode").is(calendarType)); // is("Consult-Calendar"));
		query.addCriteria(Criteria.where("customScheduleDto.scheduleFrom").lte(calendarTo.getTimeInMillis()));
		query.addCriteria(Criteria.where("customScheduleDto.scheduleTo").gte(calendarFrom.getTimeInMillis()));
		result = mongoTemplate.find(query, ParticipantScheduleDetails.class);
		return result;
	}

	@Override
	public void updateUserActive(String userName, Boolean active) throws ApplicationException {
		if (userName != null) {
			Query query = new Query();
			query.addCriteria(Criteria.where("participantUser.username").is(userName));

			List<ScheduledParticipant> result = null;

			try {
				result = this.mongoTemplate.find(query, ScheduledParticipant.class);
			} catch (Exception e) {
				e.printStackTrace();

			}

			if (result != null && result.size() > 0) {

				try {
					for (ScheduledParticipant scheduledParticipant : result) {
						scheduledParticipant.setActive(active);
					}

					this.scheduledParticipantRepository.saveAll(result);

					List<ParticipantScheduleDetails> res = this.appointmentSchedularRepository
							.findByParticipantId(String.valueOf(result.get(0).getParticipantId()));
					if (res != null) {
						for (ParticipantScheduleDetails participantScheduleDetails : res) {
							participantScheduleDetails.setActive(active);
						}
						this.appointmentSchedularRepository.saveAll(res);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Async
	public void auditAppointmentEvent(AuditEventAppoinmentDto auditEventDto, CustomerTransactionAttributeDTO custAttr,
			TokenPayLoad tokenPayload, String path) throws ApplicationException {
		MessageEvent messageEvent = null;
		try {
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
					AppUtil.convertJsonToString(auditEventDto), 1, KafkaTopics.LT_AUDIT_APPOINMENT_EVENT,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_PUBLISH, null);
			this.messageEventService.saveEvent(testEvent);
		} catch (Exception e) {
			throw new FailedException("Failed to save message event!!");
		}

		try {
			this.producerService.publishToKafka(1, KafkaTopics.LT_AUDIT_APPOINMENT_EVENT,
					AppUtil.convertJsonToString(auditEventDto), messageEvent);
		} catch (Exception e) {
//			throw new FailedException("Failed to produce kafka to LT_AUDIT_APPOINMENT_EVENT !");
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public DoctorSlots getDoctorSlot(Long doctorId, Long date, Long siteId, String path) throws ApplicationException {

		try {
			List<ParticipantScheduleDetails> result = null;

			Date fromDate = new Date(date);
			Calendar calendarFrom = Calendar.getInstance();
			calendarFrom.setTime(fromDate);
			calendarFrom.add(Calendar.HOUR_OF_DAY, 0);
			calendarFrom.add(Calendar.MINUTE, 0);
			calendarFrom.add(Calendar.SECOND, 30);
			Calendar calendarTo = Calendar.getInstance();
			calendarTo.setTime(fromDate);
			calendarTo.add(Calendar.HOUR_OF_DAY, 23);
			calendarTo.add(Calendar.MINUTE, 59);
			calendarTo.add(Calendar.SECOND, 30);

			Query query = new Query();
			query.addCriteria(Criteria.where("conductingSiteId").is(String.valueOf(siteId)));
			query.addCriteria(Criteria.where("participantId").is(String.valueOf(doctorId)));
			query.addCriteria(Criteria.where("isActive").is(true));
			query.addCriteria(Criteria.where("calendarType.identifierCode").is("Consult-Calendar"));
			query.addCriteria(Criteria.where("customScheduleDto.scheduleFrom").lte(calendarFrom.getTimeInMillis()));
			query.addCriteria(Criteria.where("customScheduleDto.scheduleTo").gte(calendarTo.getTimeInMillis()));
			result = mongoTemplate.find(query, ParticipantScheduleDetails.class);

			List<Appointment> getApptSlot = this.appointmentRepository.getDoctorAppointments(String.valueOf(doctorId),
					String.valueOf(calendarFrom.getTimeInMillis()), String.valueOf(calendarTo.getTimeInMillis()),
					siteId);

			DoctorSlots doctorSlots = null;
			if (result != null && result.size() > 0) {

				String dow = AppUtil.getDayOfWeek(new Date(date));
				doctorSlots = new DoctorSlots(String.valueOf(doctorId), null, null, AppUtil.getLocalDate(date),
						String.valueOf(siteId), 0, 0, null);
				List<Slot> slots = new ArrayList<>();

				for (ParticipantScheduleDetails res : result) {
					List<SchedulerEvent> scheduleEventData = this.schedulerEventRepository
							.findByReferrenceId(res.getId());

					Date now = new Date();
					Calendar selectedDateCal = Calendar.getInstance();
					selectedDateCal.setTime(new Date(date));

					Calendar todayCal = Calendar.getInstance();

					boolean isToday = selectedDateCal.get(Calendar.YEAR) == todayCal.get(Calendar.YEAR)
							&& selectedDateCal.get(Calendar.MONTH) == todayCal.get(Calendar.MONTH)
							&& selectedDateCal.get(Calendar.DAY_OF_MONTH) == todayCal.get(Calendar.DAY_OF_MONTH);

					scheduleEventData.forEach(slotEventdata -> {
						if ("OPEN".equals(slotEventdata.getEventData().getEventStatus().toString())) {

							Date startTimeDate = slotEventdata.getEventData().getStartTime();
							Date endTimeDate = slotEventdata.getEventData().getEndTime();

							if (isToday) {
								Calendar nowCal = Calendar.getInstance();
								nowCal.setTime(now);

								Calendar startCal = Calendar.getInstance();
								startCal.setTime(startTimeDate);

								int nowHour = nowCal.get(Calendar.HOUR_OF_DAY);
								int nowMinute = nowCal.get(Calendar.MINUTE);
								int startHour = startCal.get(Calendar.HOUR_OF_DAY);
								int startMinute = startCal.get(Calendar.MINUTE);

								if ((startHour < nowHour) || (startHour == nowHour && startMinute < nowMinute)) {
									return;
								}
							}

							Calendar cal = Calendar.getInstance();
							cal.setTime(startTimeDate);

							Calendar cal2 = Calendar.getInstance();
							cal2.setTime(endTimeDate);

							Slot slot = new Slot(slotEventdata.getId(), AppUtil.getLocalTime(cal, 0),
									AppUtil.getLocalTime(cal2, 0), SlotStatus.AVAILABLE, SlotReservation.ONLINE, 0, 0,
									getLocalDateTime(startTimeDate, cal, 0), getLocalDateTime(endTimeDate, cal2, 0));

							slots.add(slot);
						}
					});

					doctorSlots.setSlots(slots);
				}

			}

			return doctorSlots;
		} catch (Exception e) {
			// TODO: handle exception
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
			return null;
		}

	}

	@Override
	public List<ParticipantScheduleDetails> getSpecialityConfiguration(Long siteId, String calendarType,
			String participantType, String participantId, List<String> scheduleType, Long customerId, String simpleName)
			throws ApplicationException {
		Query query = new Query();
		query.addCriteria(Criteria.where("isActive").is(true));

		if (customerId != null) {
			query.addCriteria(Criteria.where("customerId").is(customerId));
		}

		if (scheduleType != null && scheduleType.size() > 0) {

			query.addCriteria(Criteria.where("scheduleType").in(scheduleType));
		}

		if (siteId > 0) {
			query.addCriteria(Criteria.where("conductingSiteId").is(String.valueOf(siteId)));
		}

		query.addCriteria(Criteria.where("calendarType.identifierCode").in(calendarType));

		if (!AppUtils.isNullString(participantType)) {
			query.addCriteria(Criteria.where("participantType.identifierCode").is(participantType));
		}

		if (!AppUtils.isNullString(participantId)) {
			query.addCriteria(Criteria.where("participantId").is(participantId));

		}

		query.fields().include("participantType", "participantName", "duration", "calendarType", "customScheduleDto",
				"slotType", "participantId", "conductingSiteId");

		List<ParticipantScheduleDetails> result = this.mongoTemplate.find(query, ParticipantScheduleDetails.class);

		return result;
	}

	@Override
	public void updateUserLoginStatus(AuditEventDto auditEvent, boolean isLogin) throws ApplicationException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonData = mapper.readTree(auditEvent.getData());
			String username = jsonData.has("username") ? jsonData.get("username").asText() : null;

			if (username != null) {
				Query query = new Query();
				query.addCriteria(Criteria.where("participantUser.username").is(username));

				ScheduledParticipant participantToUpdate = mongoTemplate.findOne(query, ScheduledParticipant.class);
				if (participantToUpdate != null) {
					participantToUpdate.setIsLogin(isLogin);
					mongoTemplate.save(participantToUpdate);
				}
			}
		} catch (Exception e) {
			throw new FailedException("Failed to save user Login Status : " + e);
		}
	}

}