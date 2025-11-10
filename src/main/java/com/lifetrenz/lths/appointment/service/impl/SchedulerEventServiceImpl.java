/**
 * 
 */
package com.lifetrenz.lths.appointment.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.common.app.exception.FailedException;
import com.lifetrenz.lths.appointment.common.app.exception.NotFoundException;
import com.lifetrenz.lths.appointment.common.enums.KafkaTopic;
import com.lifetrenz.lths.appointment.common.enums.LtAppointmentEventType;
import com.lifetrenz.lths.appointment.common.enums.MessageEventStatus;
import com.lifetrenz.lths.appointment.common.enums.MessageRequestStatus;
import com.lifetrenz.lths.appointment.common.service.StreamingService;
import com.lifetrenz.lths.appointment.common.util.AppUtils;
import com.lifetrenz.lths.appointment.common.util.KafkaTopics;
import com.lifetrenz.lths.appointment.dto.BlockEventDto;
import com.lifetrenz.lths.appointment.dto.BlockSchedularDto;
import com.lifetrenz.lths.appointment.dto.BlockScheduleStatusDto;
import com.lifetrenz.lths.appointment.dto.LtAppointmentEventDto;
import com.lifetrenz.lths.appointment.dto.ReferrenceIdDTO;
import com.lifetrenz.lths.appointment.dto.SchedulerEventDto;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.dto.UpdateMeetingScheduledDto;
import com.lifetrenz.lths.appointment.dto.UpdateScheduleEventDto;
import com.lifetrenz.lths.appointment.dto.WaitingAppointmentDto;
import com.lifetrenz.lths.appointment.mapper.EventsMapper;
import com.lifetrenz.lths.appointment.mapper.ScheduleEventMapper;
import com.lifetrenz.lths.appointment.model.collection.Appointment;
import com.lifetrenz.lths.appointment.model.collection.BlockSchedular;
import com.lifetrenz.lths.appointment.model.collection.BlockSchedularEvent;
import com.lifetrenz.lths.appointment.model.collection.SchedulerEvent;
import com.lifetrenz.lths.appointment.model.collection.SchedulerEventData;
import com.lifetrenz.lths.appointment.model.enums.BlockStatus;
import com.lifetrenz.lths.appointment.model.enums.ScheduleEventStatus;
import com.lifetrenz.lths.appointment.model.events.MessageEvent;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;
import com.lifetrenz.lths.appointment.model.value_object.ParticipantDetails;
import com.lifetrenz.lths.appointment.model.value_object.SystemMasterNew;
import com.lifetrenz.lths.appointment.model.value_object.appointment.Participant;
import com.lifetrenz.lths.appointment.repository.command.IAppointmentRepository;
import com.lifetrenz.lths.appointment.repository.command.IBlockSchedular;
import com.lifetrenz.lths.appointment.repository.command.IBlockSchedularEvent;
import com.lifetrenz.lths.appointment.repository.command.ISchedulerEventRepository;
import com.lifetrenz.lths.appointment.repository.query.SchedulerEventRepository;
import com.lifetrenz.lths.appointment.service.MessageEventService;
import com.lifetrenz.lths.appointment.service.ProducerService;
import com.lifetrenz.lths.appointment.service.SchedulerEventService;
import com.lifetrenz.lths.appointment.util.AppUtil;

/**
 * @author Ajith.K
 *
 */
@Component
public class SchedulerEventServiceImpl implements SchedulerEventService {

	private static final Logger log = LoggerFactory.getLogger(SchedulerEventServiceImpl.class);

	@Autowired
	ISchedulerEventRepository schedulerEventRepository;

	@Autowired
	SchedulerEventRepository schedulerEventRepo;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ScheduleEventMapper scheduleEventMapper;

	@Autowired
	StreamingService streamingService;

	@Autowired
	IBlockSchedular blockSchedular;

	@Autowired
	IBlockSchedularEvent blockSchedularEvent;

	@Autowired
	EventsMapper eventsMapper;

	@Autowired
	MessageEventService messageEventService;

	@Autowired
	ProducerService producerService;

	@Autowired
	IAppointmentRepository appointmentRepo;

	@Override
	public SchedulerEvent saveScheuleEvent(SchedulerEvent schedulerEvent, TokenPayLoad tokenPayload, String path)
			throws SchedulerEventException {
		try {
			if (tokenPayload != null && schedulerEvent.getCustomerTransaction() != null) {
				CustomerTransactionBase transaction = schedulerEvent.getCustomerTransaction();
				transaction.setCreatedBy(tokenPayload.getName());
				transaction.setCreatedById(tokenPayload.getCoreUserId());
				transaction.setCustomerId(tokenPayload.getCustomerId());
				transaction.setCustomerBusinessId(tokenPayload.getCustomerBusinessId());
				transaction.setCreatedOn(new Date());
			}

			SchedulerEventData eventData = schedulerEvent.getEventData();
			if (eventData != null) {
				eventData.setId(eventData.getStartTime().getTime());
				if (Boolean.TRUE.equals(eventData.getIsBlock())) {
					eventData.setRankId("1");
					eventData.setEventStatus(ScheduleEventStatus.NON_AVAILABLE);
				} else {
					eventData.setRankId("3");
					eventData.setEventStatus(ScheduleEventStatus.OPEN);
				}
			}

			return this.schedulerEventRepository.save(schedulerEvent);
		} catch (Exception e) {
			log.error("Failed to save scheduler event: {}", e.getMessage(), e);
			try {
				MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
						MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
						MessageEventStatus.FAILED_ON_CODE, e.getMessage());
				this.messageEventService.saveEvent(testEvent);
			} catch (ApplicationException ex) {
				log.error("Failed to save message event: {}", ex.getMessage(), ex);
			}
			throw new SchedulerEventException("An error occurred while saving the scheduler event.", e);
		}
	}

	@Override
	public List<SchedulerEventDto> getScheduleEvent(Long siteId, String[] calendarType, String participant,
			Long scheduleFrom, Long scheduleTo, String participantType, String path) throws Exception {

		try {
			// Base criteria (reused for both queries)
			Criteria baseCriteria = new Criteria().and("customerTransaction.active").is(true).and("conductingSiteId")
					.is(siteId).and("calendarType.identifierCode").in(Arrays.asList(calendarType))
					.and("participant.appointmentParticipantType.identifierCode").is(participantType);

			if (!AppUtil.isNullString(participant)) {
				baseCriteria.and("participant.participantId").is(participant);

			}

			// Query for non-"BOOKED" events
			Query queryNonBooked = new Query(baseCriteria);
			if (!Arrays.asList(calendarType).contains("TB-Calendar")) {
				queryNonBooked.addCriteria(Criteria.where("eventData.eventStatus").ne("BOOKED"));
			}

			List<SchedulerEventDto> finalResults = this.mongoTemplate.find(queryNonBooked, SchedulerEvent.class)
					.stream().map(scheduleEventMapper::mapToDto).collect(Collectors.toCollection(ArrayList::new));

			// Query for "BOOKED" events with optional date range
			Query queryBooked = new Query(baseCriteria);
			queryBooked.addCriteria(Criteria.where("eventData.eventStatus").is("BOOKED"));

			if (scheduleFrom != null && scheduleTo != null) {
				try {
					queryBooked.addCriteria(Criteria.where("eventData.startTime").gte(new Date(scheduleFrom)));
					queryBooked.addCriteria(Criteria.where("eventData.endTime").lte(new Date(scheduleTo)));
				} catch (Exception e) {
					throw new Exception("Error processing date range", e);
				}
			}

			// Add "BOOKED" event DTOs to the final results
			this.mongoTemplate.find(queryBooked, SchedulerEvent.class).stream().map(scheduleEventMapper::mapToDto)
					.forEach(finalResults::add);

			return finalResults;
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
	public List<SchedulerEventDto> updateEventData(String id, SchedulerEventData schedulerEventData,
			TokenPayLoad tokenPayload, String path) throws ApplicationException {
		try {
			List<SchedulerEvent> schedulerEventArr;
			try {
				schedulerEventArr = this.schedulerEventRepository.findByReferrenceId(id);
			} catch (Exception e) {
				throw new NotFoundException("Scheduler event not found!!");
			}

			for (SchedulerEvent schedulerEvent : schedulerEventArr) {
				schedulerEvent.getCustomerTransaction().setUpdatedBy(tokenPayload.getName());
				schedulerEvent.getCustomerTransaction().setUpdatedById(tokenPayload.getCoreUserId());
				schedulerEvent.getCustomerTransaction().setUpdatedOn(new Date());

				if (schedulerEvent.getEventData() != null && schedulerEventData.getRecurrenceException() != null
						&& !AppUtils.isNullString(schedulerEventData.getRecurrenceException())) {

					String re = schedulerEvent.getEventData().getRecurrenceException();
					re = (re == null || re.isEmpty()) ? schedulerEventData.getRecurrenceException()
							: re + "," + schedulerEventData.getRecurrenceException();
					schedulerEvent.getEventData().setRecurrenceException(re);

					List<SchedulerEventData> changedRecords = schedulerEvent.getChangedRecords();
					if (changedRecords == null) {
						changedRecords = new ArrayList<>();
						schedulerEvent.setChangedRecords(changedRecords);
					}

					boolean isExist = changedRecords.stream().anyMatch(record -> record.getRecurrenceException()
							.equals(schedulerEventData.getRecurrenceException()));

					if (!isExist) {
						schedulerEventData.setResourceId(schedulerEvent.getEventData().getResourceId());
						changedRecords.add(schedulerEventData);
					}

				} else if (schedulerEvent.getEventData() != null) {
					SchedulerEventData eventData = schedulerEvent.getEventData();
					eventData.setSubject(schedulerEventData.getSubject());
					eventData.setStartTime(schedulerEventData.getStartTime());
					eventData.setEndTime(schedulerEventData.getEndTime());
					eventData.setIsAllDay(schedulerEventData.getIsAllDay());
					eventData.setRecurrenceRule(schedulerEventData.getRecurrenceRule());
					eventData.setDescription(schedulerEventData.getDescription());
				}

				try {
					this.schedulerEventRepository.save(schedulerEvent);
				} catch (Exception e) {
					throw new FailedException("Scheduler event not found!!");
				}
			}

			return this.schedulerEventRepository.findByReferrenceId(id).stream().map(scheduleEventMapper::mapToDto)
					.collect(Collectors.toList());
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
	public List<SchedulerEventDto> deleteEventById(String id, SchedulerEventData obj, TokenPayLoad tokenPayload,
			String path) throws ApplicationException {
		try {
			List<SchedulerEvent> res = this.schedulerEventRepository.findByReferrenceId(id);
			for (int i = 0; i < res.size(); i++) {
				res.get(i).getEventData().setRecurrenceException(obj.getRecurrenceException());
			}

			return this.schedulerEventRepository.saveAll(res).stream().map(scheduleEventMapper::mapToDto)
					.collect(Collectors.toList());
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
	public List<SchedulerEvent> deleteEventBySchedule(String id, TokenPayLoad tokenPayload)
			throws ApplicationException {
		List<SchedulerEvent> res = this.schedulerEventRepository.findByReferrenceId(id);
		for (int i = 0; i < res.size(); i++) {
			res.get(i).getCustomerTransaction().setActive(false);
		}

		this.schedulerEventRepository.saveAll(res);
		return null;
	}

	public void updateWaitingListSession(SchedulerEvent events) {
		List<SchedulerEvent> res = this.schedulerEventRepository.findByReferrenceId(events.getReferrenceId());
		if (res != null && res.size() > 0) {
			for (SchedulerEvent schedulerEvent : res) {
				schedulerEvent.getEventData()
						.setConsumedWaitingPerSession(events.getEventData().getConsumedWaitingPerSession() + 1);
			}

			this.schedulerEventRepository.saveAll(res);
		}

	}

	@SuppressWarnings("unused")
	@Override
	public SchedulerEvent createAppointmentEvent(Appointment appointment) throws ApplicationException {
		SchedulerEvent events = null;
		try {
			events = this.schedulerEventRepository.findById(appointment.getSlotId()).get();

		} catch (Exception e) {
			e.printStackTrace();
		}
		Long recurrenceId = null;
		if (events == null) {

			throw new NotFoundException("Event not found");
		} else {
			recurrenceId = events.getEventData().getId();
		}

		List<SchedulerEvent> parentEvent = null;
		if (events != null) {
			String exe = events.getEventData().getRecurrenceException();
			if (exe == null || exe == "") {
				exe = AppUtil.getRecurrenceException(appointment.getStartDate());
			} else {
				exe = exe + "," + AppUtil.getRecurrenceException(appointment.getStartDate());
			}
			events.getEventData().setRecurrenceException(exe);
			if (appointment.getIsWaitingList()) {
				parentEvent = this.getEventByEventDataId(events.getEventData().getId());
				// to update parent event
				if (parentEvent != null && parentEvent.size() > 0) {
					for (SchedulerEvent schedulerEvent : parentEvent) {
						recurrenceId = schedulerEvent.getEventData().getId();
						schedulerEvent.getEventData()
								.setConsumedWaitingPerSlot(events.getEventData().getConsumedWaitingPerSlot() + 1);
					}
					this.schedulerEventRepository.saveAll(parentEvent);

					events.getEventData()
							.setConsumedWaitingPerSlot(events.getEventData().getConsumedWaitingPerSlot() + 1);

				}

				events = this.schedulerEventRepository.save(events);
			}

			String patName = "";
			String patId = "";
			if (appointment.getParticpantCalendar() != null && appointment.getParticpantCalendar().size() > 0) {

				Optional<ParticipantDetails> patientParticipant = appointment.getParticpantCalendar().stream()
						.filter(p -> p.getAppointmentParticipantType() != null
								&& "Patient".equals(p.getAppointmentParticipantType().getIdentifierCode()))
						.findFirst();

				patName = patientParticipant.map(ParticipantDetails::getParticipantName).orElse("");
				patId = patientParticipant.map(ParticipantDetails::getParticipantId).orElse("");

				for (ParticipantDetails participant : appointment.getParticpantCalendar()) {
					if (participant.getAppointmentParticipantType().getIdentifierCode().equals("Patient"))
						continue;

					Calendar calendarStart = Calendar.getInstance();
					calendarStart.setTime(appointment.getStartDate());
					calendarStart.set(Calendar.SECOND, 1);

					Long nextWaitingListNumber = 0L;
					if (appointment.getIsWaitingList() != null && appointment.getIsWaitingList()) {
						nextWaitingListNumber = (long) getNextWaitingListNumber(appointment.getStartDate(),
								appointment.getAppointmentSite().getId());
					}

					SchedulerEventData dataEve = new SchedulerEventData(
							appointment.getStartDate().getTime() + new Date().getTime(), calendarStart.getTime(),
							appointment.getEndDate(), patName,
							appointment.getIsWaitingList() == null ? false : appointment.getIsWaitingList(), null, null,
							null, recurrenceId, null, null, null, null, null, ScheduleEventStatus.BOOKED, false, "2",
							appointment.getId(), patId,
							appointment.getAppointmentConductMode() != null
									? appointment.getAppointmentConductMode().getIdentifierCode()
									: null,
							events.getEventData().getMaximumWaitingList(), events.getEventData().getMaxWaitingPerSlot(),
							events.getEventData().getConsumedWaitingPerSession(),
							events.getEventData().getConsumedWaitingPerSlot(),
							events.getEventData().getBlockWaitingSession(), events.getEventData().getBlockWaitingSlot(),
							null, patId, null, nextWaitingListNumber,events.getEventData().getVisitTypeIdentifier(),events.getEventData().getVisitTypeName()
							,events.getEventData().getAvgMaintainceTime());

					SchedulerEvent item = new SchedulerEvent(null, events.getCustomerTransaction(), dataEve,
							events.getReferrenceId(), events.getCalendarType(),
							new Participant(participant.getParticipantId(), participant.getParticipantName(),
									new SystemMasterNew(
											participant.getAppointmentParticipantType().getId() == null ? null
													: participant.getAppointmentParticipantType().getId(),
											participant.getAppointmentParticipantType().getNameEn() == null ? null
													: participant.getAppointmentParticipantType().getNameEn(),
											participant.getAppointmentParticipantType().getNameEn() == null ? null
													: participant.getAppointmentParticipantType().getNameEn(),
											participant.getAppointmentParticipantType().getDescription() == null ? null
													: participant.getAppointmentParticipantType().getDescription(),
											participant.getAppointmentParticipantType().getIdentifierCode() == null
													? null
													: participant.getAppointmentParticipantType().getIdentifierCode(),
											null, null)),
							events.getConductingSiteId(), null, events.getScheduleType(), null);

					try {
						item = this.schedulerEventRepository.save(item);
						if (appointment.getIsWaitingList()) {
							this.updateWaitingListSession(events);
						}
					} catch (Exception e) {
						throw new FailedException("Failed to save SchedulerEvent!!");
					}

				}

			}
		} else {
			new NotFoundException("Event not for slot id " + appointment.getSlotId());
		}

		return events;
	}

	@Override
	public SchedulerEvent updateScheuleEvent(SchedulerEvent item, int index) throws Exception {
		List<SchedulerEvent> res = this.schedulerEventRepository.findByReferrenceId(item.getReferrenceId());
		SchedulerEvent schedulerEvent = res.get(index);

		schedulerEvent.setParticipant(item.getParticipant());
		schedulerEvent.getEventData().setResourceId(item.getParticipant().getParticipantId());
		schedulerEvent = this.schedulerEventRepository.save(schedulerEvent);

		return schedulerEvent;
	}

	public List<SchedulerEvent> getScheduleEventByAppointmentId(String appId) throws ApplicationException {
		List<SchedulerEvent> res = null;
		res = this.schedulerEventRepository.findByEventData_AppointmentId(appId);

		return res;
	}

	@Override
	public List<SchedulerEvent> deleteEventByAppointmentId(String appointmentId) throws ApplicationException {
		List<SchedulerEvent> res = null;
		res = getScheduleEventByAppointmentId(appointmentId);
		if (res != null && res.size() > 0) {
			for (SchedulerEvent schedulerEvent : res) {
				schedulerEvent.getCustomerTransaction().setActive(false);
			}
			List<SchedulerEvent> parentEvent = this.getEventByEventDataId(res.get(0).getEventData().getRecurrenceID());
			if (parentEvent != null && parentEvent.size() > 0) {
				for (SchedulerEvent schedulerEvent : parentEvent) {
					if (!AppUtil.isNullString(schedulerEvent.getEventData().getRecurrenceException())) {
						String recExc = schedulerEvent.getEventData().getRecurrenceException();
						String eveExc = AppUtil.getRecurrenceException(schedulerEvent.getEventData().getStartTime());
						String exc = recExc.replace(eveExc, "");
						schedulerEvent.getEventData().setRecurrenceException(exc);
					}

				}
				this.schedulerEventRepository.saveAll(parentEvent);
			}
			this.schedulerEventRepository.saveAll(res);

			// to update waiting list appointment on cancel
			LocalDate eventDate = res.get(0).getEventData().getStartTime().toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();

			LocalDate today = LocalDate.now();

			if (eventDate.isAfter(today)) {
				updateWaitingAppointment(res.get(0), appointmentId);
			}

//			if (res.get(0).getEventData().getStartTime().after(new Date())) {
//			    updateWaitingAppointment(res.get(0), appointmentId);
//			}

		} else {
			new NotFoundException("Event not found ");
		}
		return res;
	}

	private void updateWaitingAppointment(SchedulerEvent event, String appointmentId) {
		List<SchedulerEvent> waitingEvents = this.getWaitingListEvent(event.getEventData().getStartTime(),
				event.getConductingSiteId());
		if (waitingEvents != null && waitingEvents.size() > 0) {
			SchedulerEvent minWaitingEvent = waitingEvents.stream()
					.min(Comparator.comparingLong(e -> e.getEventData().getWaitingNumber())).orElse(null);

			if (minWaitingEvent != null) {
				minWaitingEvent.getEventData().setWaitingNumber(0L);
				minWaitingEvent.getEventData().setIsAllDay(false);
				minWaitingEvent.getEventData().setStartTime(event.getEventData().getStartTime());
				minWaitingEvent.getEventData().setEndTime(event.getEventData().getEndTime());
				this.schedulerEventRepository.save(minWaitingEvent);

				try {
					Appointment app = appointmentRepo.findById(appointmentId).get();

					app.setIsWaitingList(false);
					app.setStartDate(minWaitingEvent.getEventData().getStartTime());
					app.setEndDate(minWaitingEvent.getEventData().getEndTime());
					appointmentRepo.save(app);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try {
					LtAppointmentEventDto waitingDto = new LtAppointmentEventDto();
					waitingDto.setType(LtAppointmentEventType.WAITING_STATUS.value);
					WaitingAppointmentDto waitingAppointment = new WaitingAppointmentDto();
					waitingAppointment.setAppointmentId(minWaitingEvent.getEventData().getAppointmentId());
					waitingAppointment.setStartDate(minWaitingEvent.getEventData().getStartTime());
					waitingAppointment.setEndDate(minWaitingEvent.getEventData().getEndTime());
					waitingDto.setData(AppUtils.convertJsonToString(waitingAppointment));
					MessageEvent messageEvent = this.eventsMapper.convertToMessageEvent(
							AppUtil.convertJsonToString(waitingDto), KafkaTopic.LT_APPOINTMENT_EVENT.value,
							KafkaTopics.LT_APPOINTMENT_EVENT.toString(), null, "");

					this.producerService.publishToKafka(Integer.valueOf(KafkaTopic.LT_APPOINTMENT_EVENT.value),
							KafkaTopics.LT_APPOINTMENT_EVENT.toString(), AppUtils.convertJsonToString(waitingDto),
							messageEvent);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		}
	}

	@Override
	public List<SchedulerEvent> getEventByEventDataId(Long eveDataId) throws ApplicationException {
		Query query = new Query();

		query.addCriteria(Criteria.where("eventData._id").is(eveDataId));

		List<SchedulerEvent> result = this.mongoTemplate.find(query, SchedulerEvent.class);

		return result;
	}

	@Override
	public void saveSchedulerEvent(SchedulerEvent schedEvent) throws ApplicationException {
		this.schedulerEventRepository.save(schedEvent);
	}

	@Override
	public List<SchedulerEvent> getScheduleEventEntityByReference(String referenceId) throws ApplicationException {
		Query query = new Query();

		query.addCriteria(Criteria.where("customerTransaction.active").is(true));
		query.addCriteria(Criteria.where("referrenceId").is(referenceId));

		return this.mongoTemplate.find(query, SchedulerEvent.class);
	}

	@Override
	public List<SchedulerEventDto> getUserSchedulerEvent(Long siteId, String[] calendarType, String participant,
			Long scheduleFrom, Long scheduleTo, String participantType, String path) throws Exception {

		try {
			// Base criteria (reused for both queries)

			Criteria baseCriteria = new Criteria().and("customerTransaction.active").is(true).and("conductingSiteId")
					.is(siteId).and("participant.participantId").is(participant)
					.and("participant.appointmentParticipantType.identifierCode").is(participantType);

			// Query for non-"BOOKED" events
			Query queryNonBooked = new Query(baseCriteria);
			queryNonBooked.addCriteria(Criteria.where("calendarType.identifierCode").ne("Consult-Calendar"));
			List<SchedulerEventDto> finalResults = this.mongoTemplate.find(queryNonBooked, SchedulerEvent.class)
					.stream().map(scheduleEventMapper::mapToDto).collect(Collectors.toCollection(ArrayList::new));

			// Query for "BOOKED" events with optional date range
			Query queryBooked = new Query(baseCriteria);
			queryBooked.addCriteria(Criteria.where("calendarType.identifierCode").is("Consult-Calendar"));
			queryBooked.addCriteria(Criteria.where("eventData.eventStatus").is("BOOKED"));

			if (scheduleFrom != null && scheduleTo != null) {
				try {
					queryBooked.addCriteria(Criteria.where("eventData.startTime").gte(new Date(scheduleFrom)));
					queryBooked.addCriteria(Criteria.where("eventData.endTime").lte(new Date(scheduleTo)));
				} catch (Exception e) {
					throw new Exception("Error processing date range", e);
				}
			}

			// Add "BOOKED" event DTOs to the final results
			this.mongoTemplate.find(queryBooked, SchedulerEvent.class).stream().map(scheduleEventMapper::mapToDto)
					.forEach(finalResults::add);

			return finalResults;
		} catch (Exception e) {
			// TODO: handle exception
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE, e.getMessage());

			this.messageEventService.saveEvent(testEvent);
			return null;
		}

	}

	@SuppressWarnings("unused")
	@Override
	public SchedulerEventDto blockEvents(BlockEventDto eventBlock, String path) throws ApplicationException {
		try {
			SchedulerEvent events = this.schedulerEventRepository.findById(eventBlock.getSlotId()).get();
			Long recurrenceId = null;
			if (events == null) {
				throw new NotFoundException("Event not found");
			} else {
				recurrenceId = events.getEventData().getId();
			}

			if (events != null) {
				List<SchedulerEvent> blockSlots = this.schedulerEventRepository
						.findByEventData_IdAndEventDataIsBlock(recurrenceId, true);
				if (blockSlots != null && blockSlots.size() > 0) {
					for (SchedulerEvent schedulerEvent : blockSlots) {
						schedulerEvent.getEventData().setEventStatus(ScheduleEventStatus.BLOCKED);
					}
					blockSlots = this.schedulerEventRepository.saveAll(blockSlots);

					CustomerTransactionBase cb = new CustomerTransactionBase();
					cb = eventBlock.getCustomerTrasaction();
					cb.setUpdatedById(eventBlock.getCustomerTrasaction().getCreatedById());
					cb.setUpdatedBy(eventBlock.getCustomerTrasaction().getCreatedBy());
					cb.setUpdatedOn(new Date());

					BlockSchedularEvent blockSchedularEventDto = new BlockSchedularEvent(null, eventBlock.getUserName(),
							eventBlock.getUserId(), eventBlock.getPatientName(), eventBlock.getPatientId(),
							eventBlock.getSlotId(), "UPDATE", new Date(eventBlock.getStartDate()),
							eventBlock.getStatus(), cb);
					this.blockSchedularEvent.save(blockSchedularEventDto);
				} else {
					String exe = events.getEventData().getRecurrenceException();
					if (exe == null || exe == "") {
						exe = AppUtil.getRecurrenceException(new Date(eventBlock.getStartDate()));
					} else {
						exe = exe + "," + AppUtil.getRecurrenceException(new Date(eventBlock.getStartDate()));
					}
					events.getEventData().setRecurrenceException(exe);

					SchedulerEventData dataEve = new SchedulerEventData();
					dataEve.setId(new Date(eventBlock.getStartDate()).getTime() + new Date().getTime());
					Calendar calendarStart = Calendar.getInstance();
					calendarStart.setTime(new Date(eventBlock.getStartDate()));
					calendarStart.set(Calendar.SECOND, 1);
					dataEve.setStartTime(calendarStart.getTime());
					dataEve.setEndTime(new Date(eventBlock.getEndDate()));
					dataEve.setRecurrenceID(recurrenceId);
					dataEve.setSubject(eventBlock.getPatientName());
					dataEve.setPatientId(String.valueOf(eventBlock.getPatientId()));
					dataEve.setConductMode(null);
					dataEve.setIsAllDay(false);
					dataEve.setIsBlock(true);
					dataEve.setEventStatus(ScheduleEventStatus.BLOCKED);
					dataEve.setRankId("2");
					dataEve.setAppointmentId(null);
					dataEve.setMaximumWaitingList(events.getEventData().getMaximumWaitingList());
					dataEve.setMaxWaitingPerSlot(events.getEventData().getMaxWaitingPerSlot());
					dataEve.setConsumedWaitingPerSession(events.getEventData().getConsumedWaitingPerSession());
					dataEve.setConsumedWaitingPerSlot(events.getEventData().getConsumedWaitingPerSlot());
					dataEve.setBlockWaitingSession(events.getEventData().getBlockWaitingSession());
					dataEve.setBlockWaitingSlot(events.getEventData().getBlockWaitingSlot());
					SchedulerEvent item = new SchedulerEvent(null, events.getCustomerTransaction(), dataEve,
							events.getReferrenceId(), events.getCalendarType(), events.getParticipant(),
							events.getConductingSiteId(), null, events.getScheduleType(), null);

					try {
						this.schedulerEventRepository.save(events);
						item = this.schedulerEventRepository.save(item);
						eventBlock.getCustomerTrasaction().setCreatedOn(new Date());
						BlockSchedular blockSchedularDto = new BlockSchedular(null, eventBlock.getUserName(),
								eventBlock.getUserId(), eventBlock.getPatientName(), eventBlock.getPatientId(),
								item.getId(), new Date(eventBlock.getStartDate()), eventBlock.getStatus(),
								eventBlock.getCustomerTrasaction());
						this.blockSchedular.save(blockSchedularDto);

						BlockSchedularEvent blockSchedularEventDto = new BlockSchedularEvent(null,
								eventBlock.getUserName(), eventBlock.getUserId(), eventBlock.getPatientName(),
								eventBlock.getPatientId(), eventBlock.getSlotId(), eventBlock.getEventIdentifier(),
								new Date(eventBlock.getStartDate()), eventBlock.getStatus(),
								eventBlock.getCustomerTrasaction());
						this.blockSchedularEvent.save(blockSchedularEventDto);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} else {
				new NotFoundException("Event not for slot id " + eventBlock.getSlotId());
			}

			return this.scheduleEventMapper.mapToDto(events);
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
	public List<BlockSchedularDto> getBlockSchedular(Long customerBusinessId, Long siteId, Long userId, Long fromDate,
			Long toDate, String path) throws Exception {

		try {
			Query query = new Query();

			query.addCriteria(Criteria.where("customerTrasaction.active").is(true));
			query.addCriteria(Criteria.where("customerTrasaction.customerBusinessId").is(customerBusinessId));
			query.addCriteria(Criteria.where("customerTrasaction.siteId").is(siteId));
			if (userId != null) {
				query.addCriteria(Criteria.where("userId").is(userId));
			}

			if (fromDate != null && toDate != null) {
				try {
					query.addCriteria(Criteria.where("blockedDate").gte(new Date(fromDate)).lte(new Date(toDate)));
				} catch (Exception e) {
					throw new Exception("Error processing date range", e);
				}
			}

			return this.mongoTemplate.find(query, BlockSchedularEvent.class).stream()
					.map(scheduleEventMapper::mapToBlockSchedularEventDto).collect(Collectors.toList());
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
	public List<BlockSchedularDto> getBlockSchedularHistory(String schedulerEventId, String path)
			throws ApplicationException {

		try {
			return blockSchedularEvent.findBySchedularEventId(schedulerEventId).stream()
					.map(scheduleEventMapper::mapToBlockSchedularEventDto).collect(Collectors.toList());
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
	public BlockSchedularDto updateSlotStatus(BlockScheduleStatusDto status, String path) throws ApplicationException {
		try {
			SchedulerEvent event = this.schedulerEventRepository.findById(status.getId()).get();
			if (event != null) {
				if (status.getStatus().equals(BlockStatus.BLOCKED)) {
					event.getEventData().setEventStatus(ScheduleEventStatus.BLOCKED);
				} else {
					event.getEventData().setEventStatus(ScheduleEventStatus.OPEN);
					event.getCustomerTransaction().setActive(false);
					String exe = AppUtil.getRecurrenceException(event.getEventData().getStartTime());
					List<SchedulerEvent> exeEventLst = this.schedulerEventRepo
							.getEventsByRefferenceIdAndException(event.getEventData().getRecurrenceID(), exe);
					for (SchedulerEvent exeEvent : exeEventLst) {
						SchedulerEventData data = exeEvent.getEventData();
						if (data != null && data.getRecurrenceException() != null) {
							String recurrenceException = data.getRecurrenceException();

							// Split into individual IDs
							List<String> ids = new ArrayList<>(Arrays.asList(recurrenceException.split(",")));

							// Remove the recurrenceId as string
							ids.remove(String.valueOf(exe));

							// Join back to string
							String updatedRecurrenceException = String.join(",", ids);

							// Update the value
							data.setRecurrenceException(updatedRecurrenceException);

							// Save the updated event
							mongoTemplate.save(exeEvent);
						}
					}

				}
				event = this.schedulerEventRepository.save(event);
			}
			List<BlockSchedular> res = blockSchedular.findBySchedularEventId(status.getId());
			if (res != null && res.size() > 0) {
				for (BlockSchedular blockSchedular : res) {
					blockSchedular.setStatus(status.getStatus());
					blockSchedular.getCustomerTrasaction()
							.setUpdatedById(status.getCustomerTrasaction().getCreatedById());
					blockSchedular.getCustomerTrasaction().setUpdatedBy(status.getCustomerTrasaction().getCreatedBy());
					blockSchedular.getCustomerTrasaction().setUpdatedOn(new Date());
				}
				res = blockSchedular.saveAll(res);
				BlockSchedularEvent blockSchedularEventDto = new BlockSchedularEvent(null, res.get(0).getUserName(),
						res.get(0).getUserId(), res.get(0).getPatientName(), res.get(0).getPatientId(),
						res.get(0).getSchedularEventId(), "UPDATE", res.get(0).getBlockedDate(), status.getStatus(),
						res.get(0).getCustomerTrasaction());
				this.blockSchedularEvent.save(blockSchedularEventDto);
			}

			return res == null ? null : scheduleEventMapper.mapToBlockSchedularDto(res.get(0));
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
	public void updateCommiteeMeetingSchedule(UpdateMeetingScheduledDto updateMeetingScheduledDto)
			throws ApplicationException {
		try {
			// Extract reference IDs from DTO
			List<String> referrenceIds = updateMeetingScheduledDto.getReferrenceIds().stream()
					.map(ReferrenceIdDTO::getReferrenceId).collect(Collectors.toList());

			// Iterate over the reference IDs to find matching scheduler events
			for (String referrenceId : referrenceIds) {
				List<SchedulerEvent> schedulerEventArr = this.schedulerEventRepository
						.findByReferrenceIdAndParticipant_ParticipantId(referrenceId,
								updateMeetingScheduledDto.getOldUserId().toString());

				if (schedulerEventArr.isEmpty()) {
					throw new NotFoundException("Scheduler events not found for referrenceId: " + referrenceId);
				}

				for (SchedulerEvent schedulerEvent : schedulerEventArr) {

					schedulerEvent.getParticipant().setParticipantId(updateMeetingScheduledDto.getUserId().toString());
					schedulerEvent.getParticipant().setParticipantName(updateMeetingScheduledDto.getFullName());
					schedulerEvent.getEventData().setResourceId(updateMeetingScheduledDto.getUserId().toString());

					// Save updated scheduler event
					try {
						this.schedulerEventRepository.save(schedulerEvent);
					} catch (Exception e) {
						throw new FailedException("Failed to update SchedulerEvent: " + schedulerEvent.getId());
					}
				}
			}
		} catch (Exception e) {
			throw new FailedException("Error while updating committee meeting schedule");
		}

	}

	public int getNextWaitingListNumber(Date eventStartTime, Long siteId) {
		LocalDate localDate = eventStartTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		ZonedDateTime startOfDayZdt = localDate.atStartOfDay(ZoneId.systemDefault());
		ZonedDateTime endOfDayZdt = localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault());

		Date startOfDay = Date.from(startOfDayZdt.toInstant());
		Date endOfDay = Date.from(endOfDayZdt.toInstant());

		List<SchedulerEvent> events = schedulerEventRepository.findByStartTimeBetweenAndSiteAndIsAllDayTrue(startOfDay,
				endOfDay, siteId);

		return events.size() + 1;
	}

	public List<SchedulerEvent> getWaitingListEvent(Date eventStartTime, Long siteId) {
		LocalDate localDate = eventStartTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		ZonedDateTime startOfDayZdt = localDate.atStartOfDay(ZoneId.systemDefault());
		ZonedDateTime endOfDayZdt = localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault());

		Date startOfDay = Date.from(startOfDayZdt.toInstant());
		Date endOfDay = Date.from(endOfDayZdt.toInstant());

		List<SchedulerEvent> events = schedulerEventRepository.findByStartTimeBetweenAndSiteAndIsAllDayTrue(startOfDay,
				endOfDay, siteId);

		return events;
	}

	@Override
	public UpdateScheduleEventDto updateScheduleEventStatus(UpdateScheduleEventDto updateScheduleEventDto,
			String simpleName) throws Exception {

		Optional<Appointment> appointmentOpt = this.appointmentRepo.findById(updateScheduleEventDto.getAppointmentId());
		if (!appointmentOpt.isPresent()) {
			throw new NotFoundException("Appointment not found for ID: " + updateScheduleEventDto.getAppointmentId());
		}
		Appointment appointment = appointmentOpt.get();

		List<SchedulerEvent> events = this.schedulerEventRepository.findByEventData_AppointmentId(appointment.getId());
		if (events.isEmpty()) {
			throw new NotFoundException("No SchedulerEvents found for appointment ID: " + appointment.getId());
		}

		for (SchedulerEvent event : events) {
			SchedulerEventData eventData = event.getEventData();
			if (updateScheduleEventDto.getAppointmentStatus() != null
					&& !updateScheduleEventDto.getAppointmentStatus().isEmpty()) {
				try {
					eventData
							.setEventStatus(ScheduleEventStatus.valueOf(updateScheduleEventDto.getAppointmentStatus()));
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException(
							"Invalid appointment status: " + updateScheduleEventDto.getAppointmentStatus());
				}
			}

			if (appointment.getIsWaitingList() != null && appointment.getIsWaitingList()) {
				String recurrenceException = eventData.getRecurrenceException();
				recurrenceException = (recurrenceException == null || recurrenceException.isEmpty())
						? AppUtil.getRecurrenceException(appointment.getStartDate())
						: recurrenceException + "," + AppUtil.getRecurrenceException(appointment.getStartDate());
				eventData.setRecurrenceException(recurrenceException);
				eventData.setConsumedWaitingPerSlot(
						eventData.getConsumedWaitingPerSlot() != null ? eventData.getConsumedWaitingPerSlot() + 1 : 1);

				List<SchedulerEvent> parentEvents = this.getEventByEventDataId(eventData.getId());
				if (!parentEvents.isEmpty()) {
					for (SchedulerEvent parentEvent : parentEvents) {
						SchedulerEventData parentEventData = parentEvent.getEventData();
						parentEventData.setConsumedWaitingPerSlot(parentEventData.getConsumedWaitingPerSlot() != null
								? parentEventData.getConsumedWaitingPerSlot() + 1
								: 1);
					}
					this.schedulerEventRepository.saveAll(parentEvents);
				}
			}
		}

		try {
			this.schedulerEventRepository.saveAll(events);
			if (appointment.getIsWaitingList() != null && appointment.getIsWaitingList()) {
				for (SchedulerEvent event : events) {
					this.updateWaitingListSession(event);
				}
			}
		} catch (Exception e) {
			throw new FailedException("Failed to update SchedulerEvents");
		}

		return updateScheduleEventDto;
	}
	
	@Override
	public SchedulerEventDto getNextAvailableSlot(String participantId, Date fromDate, Date toDate)
			throws ApplicationException {
		try {
			// Query to find all events for the participant within the date range
			Query query = new Query();
			query.addCriteria(Criteria.where("participant.participantId").is(participantId));
			query.addCriteria(Criteria.where("eventData.startTime").gte(fromDate).lte(toDate));

			List<SchedulerEvent> events = mongoTemplate.find(query, SchedulerEvent.class);

			// Process recurrence rules and find the next available slot
			Date currentDate = fromDate;
			while (!currentDate.after(toDate)) {
				boolean isSlotAvailable = true;

				for (SchedulerEvent event : events) {
					if (event.getEventData().getStartTime().equals(currentDate)) {
						isSlotAvailable = false;
						break;
					}
				}

				if (isSlotAvailable) {
					SchedulerEventDto availableSlot = new SchedulerEventDto();
					// availableSlot.setParticipantId(participantId);
					// availableSlot.setStartTime(currentDate);
					System.out.println("Found available slot: " + availableSlot);
					return availableSlot;
				}

				// Increment to the next slot (e.g., 20 minutes later)
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(currentDate);
				calendar.add(Calendar.MINUTE, 20);
				currentDate = calendar.getTime();
			}

			throw new NotFoundException("No available slots found within the given date range.");
		} catch (Exception e) {
			log.error("Error while finding the next available slot: {}", e.getMessage(), e);
			throw new SchedulerEventException("Error while finding the next available slot.", e);
		}
	}
}

class SchedulerEventException extends ApplicationException {

	private static final long serialVersionUID = 1L;

	public SchedulerEventException(String message) {
		super(500, message);
	}

	public SchedulerEventException(String message, Throwable cause) {
		super(500, message);
		initCause(cause);
	}
}
