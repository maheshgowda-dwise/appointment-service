/**
* 
*/
package com.lifetrenz.lths.appointment.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.common.enums.MessageEventStatus;
import com.lifetrenz.lths.appointment.common.enums.MessageRequestStatus;
import com.lifetrenz.lths.appointment.common.util.AppUtils;
import com.lifetrenz.lths.appointment.common.util.KafkaTopics;
import com.lifetrenz.lths.appointment.dto.AuditEventDto;
import com.lifetrenz.lths.appointment.dto.CancelVisitDTO;
import com.lifetrenz.lths.appointment.dto.DeleteUserDTO;
import com.lifetrenz.lths.appointment.dto.ImageDto;
import com.lifetrenz.lths.appointment.dto.KafkaAppointmentDto;
import com.lifetrenz.lths.appointment.dto.KafkaEventType;
import com.lifetrenz.lths.appointment.dto.KafkaOpdDto;
import com.lifetrenz.lths.appointment.dto.LTAKafkaTeleconsultstionDetails;
import com.lifetrenz.lths.appointment.dto.LTMBookAppointmentDto;
import com.lifetrenz.lths.appointment.dto.LtAppointmentCancelKafkaRequest;
import com.lifetrenz.lths.appointment.dto.LtRescheduleAppointmentDto;
import com.lifetrenz.lths.appointment.dto.MarkArriveStatusDTO;
import com.lifetrenz.lths.appointment.dto.NeedCloseUpdateDTO;
import com.lifetrenz.lths.appointment.dto.RelayBookAppointmentDto;
import com.lifetrenz.lths.appointment.dto.RelayEventDTO;
import com.lifetrenz.lths.appointment.dto.UpdateMeetingScheduledDto;
import com.lifetrenz.lths.appointment.dto.UserUpdateProfileDTO;
import com.lifetrenz.lths.appointment.mapper.AppointmentMapper;
import com.lifetrenz.lths.appointment.mapper.EventsMapper;
import com.lifetrenz.lths.appointment.model.collection.SchedulerEvent;
import com.lifetrenz.lths.appointment.model.events.MessageEvent;
import com.lifetrenz.lths.appointment.util.AppUtil;

/**
 * @author Ajith.K
 *
 */
@Service
public class ConsumerService {

	final static Logger log = LoggerFactory.getLogger(ConsumerService.class);

//    @Autowired
//    AppointmentConfigService appointmentConfigService;

	@Autowired
	SchedulerEventService schedulerEventService;

	@Autowired
	AppointmentSchedulerService appointmentSchedulerService;

	@Autowired
	AppointmentService appointmentService;

	@Autowired
	AppointmentMapper appointmentMapper;

	@Autowired
	TokenService tokenService;

	@Autowired
	MessageEventService messageEventService;

	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	EventsMapper eventsMapper;

	@KafkaListener(topics = { KafkaTopics.TUMOR_BOARD_MEETING_SCHEDULE })
	public void tumorBoardMeeingSchedule(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment)
			throws Exception {
		try {
			acknowledgment.acknowledge();
			String request = consumerRecord.value();
			String[] resultRequest = request.split("~");

			if (resultRequest.length > 0) {
				List<SchedulerEvent> scheduleEventList = Arrays.asList(
						objectMapper.readValue(resultRequest[0], SchedulerEvent[].class));

				scheduleEventList.forEach(item -> {
					item.setReferrenceId(resultRequest[1]);
					try {
						schedulerEventService.saveScheuleEvent(item, null, this.getClass().getSimpleName());
					} catch (ApplicationException e) {
						handleFailedEvent(scheduleEventList, e);
					}
				});
			}
		} catch (JsonProcessingException e) {
			throw new Exception("Failed to process JSON: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new Exception("An unexpected error occurred: " + e.getMessage(), e);
		}
	}

	private void handleFailedEvent(List<SchedulerEvent> scheduleEventList, ApplicationException e) {
		MessageEvent testEvent = eventsMapper.convertToMessageEvent(null,
				AppUtil.convertJsonToString(scheduleEventList), 1, KafkaTopics.TUMOR_BOARD_MEETING_SCHEDULE,
				MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
				MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
		try {
			messageEventService.saveEvent(testEvent);
		} catch (ApplicationException ex) {
			LoggerFactory.getLogger(this.getClass()).error("Failed to save message event: " + ex.getMessage(), ex);
		}
	}

	@KafkaListener(topics = { KafkaTopics.TUMOR_BOARD_MEETING_UPDATE })
	public void tumorBoardMeeingUpdate(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment)
			throws Exception {
		acknowledgment.acknowledge();
		List<SchedulerEvent> scheduleEventList;
		try {

			String request = consumerRecord.value();
			String[] resultRequest = request.split("~");
			int i = 0;
			if (resultRequest.length > 0) {
				scheduleEventList = Arrays
						.asList(new ObjectMapper().readValue(resultRequest[0], SchedulerEvent[].class));
//				scheduleEventList.forEach(item -> {
//					item.setReferrenceId(resultRequest[1]);
//					try {
//						this.schedulerEventService.updateScheuleEvent(item, i);
//					} catch (Exception e) {
//					}
//					i++;
//				});
				for (SchedulerEvent item : scheduleEventList) {
					item.setReferrenceId(resultRequest[1]);
					try {
						this.schedulerEventService.updateScheuleEvent(item, i++);
					} catch (Exception e) {
						MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
								AppUtil.convertJsonToString(scheduleEventList), 1, KafkaTopics.TUMOR_BOARD_MEETING_UPDATE,
								MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
								MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
						this.messageEventService.saveEvent(testEvent);
					}
				}

			}

		} catch (JsonMappingException e) {
			throw new Exception("Failed to save configuration");
		} catch (JsonProcessingException e) {
			throw new Exception("Failed to save configuration");
		} catch (Exception e) {
			throw new Exception("Failed to save configuration");
		}

	}

	@KafkaListener(topics = { KafkaTopics.TUMOR_BOARD_MEETING_DELETE })
	public void tumorBoardMeeingDelete(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment)
			throws Exception {
		acknowledgment.acknowledge();
//		List<SchedulerEvent> scheduleEventList;
		try {

			String request = consumerRecord.value();
			if (!AppUtils.isNullString(request)) {
				try {
					this.schedulerEventService.deleteEventBySchedule(request, null);
				} catch (ApplicationException e) {
					MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
							AppUtil.convertJsonToString(request), 1, KafkaTopics.TUMOR_BOARD_MEETING_DELETE,
							MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
							MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());

					this.messageEventService.saveEvent(testEvent);
				}

			}

		} catch (Exception e) {
			throw new Exception("Failed to delete events");
		}

	}

	@KafkaListener(topics = { KafkaTopics.COMMITTEE_MEETING_SCHEDULE })
	public void committeeMeeingSave(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment)
			throws Exception {
		acknowledgment.acknowledge();
		List<SchedulerEvent> scheduleEventList;
		try {

			String request = consumerRecord.value();
			String[] resultRequest = request.split("~");
			if (resultRequest.length > 0) {
				scheduleEventList = Arrays
						.asList(new ObjectMapper().readValue(resultRequest[0], SchedulerEvent[].class));
				scheduleEventList.forEach(item -> {
					item.setReferrenceId(resultRequest[1]);
					try {
						this.schedulerEventService.saveScheuleEvent(item, null, this.getClass().getSimpleName());
					} catch (ApplicationException e) {
						MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
								AppUtil.convertJsonToString(scheduleEventList), 1, KafkaTopics.COMMITTEE_MEETING_SCHEDULE,
								MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
								MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
						try {
							this.messageEventService.saveEvent(testEvent);
						} catch (ApplicationException e1) {
							// TODO Auto-generated catch block
//							e1.printStackTrace();
						}
					}
				});

			}

		} catch (JsonMappingException e) {
			throw new Exception("Failed to save configuration");
		} catch (JsonProcessingException e) {
			throw new Exception("Failed to save configuration");
		} catch (Exception e) {
			throw new Exception("Failed to save configuration");
		}

	}

	@KafkaListener(topics = { KafkaTopics.COMMITTEE_MEETING_SCHEDULE_DELETE })
	public void committeMeeingDelete(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment)
			throws Exception {
		acknowledgment.acknowledge();
//		List<SchedulerEvent> scheduleEventList;
		try {

			String request = consumerRecord.value();
			if (!AppUtils.isNullString(request)) {
				try {
					this.schedulerEventService.deleteEventBySchedule(request, null);
				} catch (ApplicationException e) {
					MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
							AppUtil.convertJsonToString(request), 1, KafkaTopics.COMMITTEE_MEETING_SCHEDULE_DELETE,
							MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
							MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
					this.messageEventService.saveEvent(testEvent);
				}

			}

		} catch (Exception e) {
			throw new Exception("Failed to delete events");
		}

	}

	@KafkaListener(topics = KafkaTopics.UMS_USER_PROFILE_UPDATE)
	public void userProfileUpdate(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment) {
		acknowledgment.acknowledge();
		try {
			UserUpdateProfileDTO updateUser = new UserUpdateProfileDTO();
			updateUser = new ObjectMapper().readValue(consumerRecord.value(), UserUpdateProfileDTO.class);
//			ScheduledParticipant updateUserRequest = this.eventMapper.convertToUpdateUserRequest(updateUser);
			try {
				this.appointmentSchedulerService.updateUserProfileStatus(updateUser);
			} catch (Exception e) {
				// TODO: handle exception
				MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
						AppUtil.convertJsonToString(updateUser), 1, KafkaTopics.UMS_USER_PROFILE_UPDATE,
						MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
						MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
				this.messageEventService.saveEvent(testEvent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@KafkaListener(topics = { KafkaTopics.UPDATE_USER_IMAGE_URL })
	public void updateImageUrl(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment)
			throws Exception {
		acknowledgment.acknowledge();
		ImageDto image = null;

		try {
			image = new ObjectMapper().readValue(consumerRecord.value(), ImageDto.class);
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			this.appointmentSchedulerService.updateImageForUser(image.getCoreUserId(), image.getProfilePath());

		} catch (Exception e) {
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
					AppUtil.convertJsonToString(image), 1, KafkaTopics.UPDATE_USER_IMAGE_URL,
					MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
		}

	}

	@KafkaListener(topics = KafkaTopics.LTM_APPOINTMENT_BOOK)
	public void bookAppointment(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment)
			throws Exception {
		try {
			CompletableFuture.runAsync(() -> {
				try {
					this.bookAppointment(objectMapper.readValue(consumerRecord.value(), LTMBookAppointmentDto.class));
				} catch (Exception e) {
					LoggerFactory.getLogger(this.getClass()).error("Error processing Kafka message: " + e.getMessage(), e);
				}
			});
		} catch (Exception e) {
			LoggerFactory.getLogger(this.getClass()).error("Error running async task: " + e.getMessage(), e);
		}
		acknowledgment.acknowledge();

	}

	@Async
	private void bookAppointment(LTMBookAppointmentDto kafkaPatientEventsDTO)
			throws Exception {
		try {
			this.appointmentService.bookAppoinment(kafkaPatientEventsDTO.getBookRequest(), kafkaPatientEventsDTO.getTokenPayload(), this.getClass().getSimpleName());
		} catch (Exception e) {
//			throw new Exception("Appointment failed!!");
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
					AppUtil.convertJsonToString(kafkaPatientEventsDTO), 1, KafkaTopics.LTM_APPOINTMENT_BOOK,
					MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
		}

	}

	@KafkaListener(topics = KafkaTopics.LT_APPOINTMENT_BOOK)
	public void kafkaBookAppointment(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment)
			throws Exception {
		acknowledgment.acknowledge();
		RelayBookAppointmentDto kafkaRequest = null;

		kafkaRequest = new ObjectMapper().readValue(consumerRecord.value(), RelayBookAppointmentDto.class);
		try {

			this.appointmentService.bookNewAppointment(kafkaRequest);

		} catch (ApplicationException e) {
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
					AppUtil.convertJsonToString(kafkaRequest), 1, KafkaTopics.LT_APPOINTMENT_BOOK,
					MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
		}

	}

	@KafkaListener(topics = KafkaTopics.LTA_APPOINTMENT_CANCEL_TOPIC)
	@Transactional(transactionManager = "ltTransactionManager")
	public void kafkaCancelAppointment(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment)
			throws Exception {
		try {
			CompletableFuture.runAsync(() -> {
				try {
					LtAppointmentCancelKafkaRequest kafkaRequest = null;
					kafkaRequest = new ObjectMapper().readValue(consumerRecord.value(),
							LtAppointmentCancelKafkaRequest.class);
					this.cancelAppointment(kafkaRequest);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

		} catch (Exception e) {
		}
		acknowledgment.acknowledge();

	}

	@Async
	private void cancelAppointment(LtAppointmentCancelKafkaRequest cancelDto) throws Exception {
		try {

			this.appointmentService.cancleApptnt(cancelDto, "appt", cancelDto.getUpdatedBy(), this.getClass().getSimpleName());

		} catch (ApplicationException e) {
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
					AppUtil.convertJsonToString(cancelDto), 1, KafkaTopics.LTA_APPOINTMENT_CANCEL_TOPIC,
					MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());

			this.messageEventService.saveEvent(testEvent);
		}

	}

//	@KafkaListener(topics = KafkaTopics.LTA_APPOINTMENT_RESCHEDULE_TOPIC)
//	@Transactional(transactionManager = "ltTransactionManager")
//	public void kafkaRescheduleAppointment(ConsumerRecord<Integer, String> consumerRecord,
//			Acknowledgment acknowledgment) throws Exception {
//		acknowledgment.acknowledge();
//		LtRescheduleAppointmentDto kafkaRequest = null;
//
//		kafkaRequest = new ObjectMapper().readValue(consumerRecord.value(), LtRescheduleAppointmentDto.class);
//
//		try {
//
//			this.appointmentService.rescheduleAppt(kafkaRequest, "appt");
//
//		} catch (ApplicationException e) {
//		}
//	}

	@KafkaListener(topics = KafkaTopics.LTA_TELECONSULT_DETAILS)
	@Transactional(transactionManager = "ltTransactionManager")
	public void kafkaTeleconsultDetails(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment)
			throws Exception {
		acknowledgment.acknowledge();
		LTAKafkaTeleconsultstionDetails kafkaRequest = null;

		kafkaRequest = new ObjectMapper().readValue(consumerRecord.value(), LTAKafkaTeleconsultstionDetails.class);

		try {

			this.appointmentService.setTeleconsutChennelId(kafkaRequest);

		} catch (ApplicationException e) {
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
					AppUtil.convertJsonToString(kafkaRequest), 1, KafkaTopics.LTA_TELECONSULT_DETAILS,
					MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
		}
	}

	@KafkaListener(topics = KafkaTopics.MARK_ADMISSION_DASH_BOARD_EVENT)
	public void updateOpMarkArriveStatus(ConsumerRecord<Integer, String> consumerRecord,
			Acknowledgment acknowledgment) {
		acknowledgment.acknowledge();
		String value = consumerRecord.value();
		MarkArriveStatusDTO markArriveStatus;
		try {
			markArriveStatus = new ObjectMapper().readValue(value, MarkArriveStatusDTO.class);
			try {
				this.appointmentService.updateAppointmentStatus(markArriveStatus);
			} catch (Exception e) {
				// TODO: handle exception
				MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
						AppUtil.convertJsonToString(markArriveStatus), 1, KafkaTopics.MARK_ADMISSION_DASH_BOARD_EVENT,
						MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
						MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
				this.messageEventService.saveEvent(testEvent);
			}

		} catch (JsonMappingException e1) {

		} catch (JsonProcessingException e1) {

		} catch (Exception e) {
		}

	}

	@KafkaListener(topics = KafkaTopics.LT_APPOINTMENT_SERVICE)
	public void updateDashboardStatus(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment) {
		acknowledgment.acknowledge();
		String value = consumerRecord.value();
		KafkaAppointmentDto kafkaAppointment;
		try {
			kafkaAppointment = new ObjectMapper().readValue(value, KafkaAppointmentDto.class);
			KafkaEventType type = kafkaAppointment.getKafkaType();

			switch (type) {
			case ADDMISSION_STATUS:
				try {
					this.appointmentService.updateClinicalStatus(kafkaAppointment.getAdmissionDashboardStatus());
				} catch (Exception e) {
					// TODO: handle exception
					MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
							AppUtil.convertJsonToString(kafkaAppointment), 1, KafkaTopics.LT_APPOINTMENT_SERVICE,
							MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
							MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
					this.messageEventService.saveEvent(testEvent);
				}
				break;

			case APPOINTMENT_STATUS:
				try {
					this.appointmentService.updateAppointmentStatus(kafkaAppointment.getAppointmentStatus());
				} catch (Exception e) {
					// TODO: handle exception
					MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
							AppUtil.convertJsonToString(kafkaAppointment), 1, KafkaTopics.LT_APPOINTMENT_SERVICE,
							MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
							MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
					this.messageEventService.saveEvent(testEvent);
				}
				break;

			default:
				break;
			}

		} catch (JsonMappingException e1) {

		} catch (JsonProcessingException e1) {

		} catch (Exception e) {
		}

	}

	@KafkaListener(topics = KafkaTopics.LT_AUDIT_LOGIN_AND_OUT_EVENT)
	public void consumeLoginLogoutEvent(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment) {
		acknowledgment.acknowledge();
		try {
			String value = consumerRecord.value();
			AuditEventDto auditEvent = new ObjectMapper().readValue(value, AuditEventDto.class);

			if (auditEvent != null && auditEvent.getAuditType() != null) {
				switch (auditEvent.getAuditType()) {
				case LOGIN_DETAILS:
					appointmentSchedulerService.updateUserLoginStatus(auditEvent, true);
					System.out.println("LOGIN_DETAILS");
					break;

				case LOGOUT_DETAILS:
					appointmentSchedulerService.updateUserLoginStatus(auditEvent, false);
					System.out.println("LOGOUT_DETAILS");
					break;

				default:
					log.warn("Unknown event type received: {}", auditEvent.getAuditType());
					break;
				}
			}
		} catch (Exception e) {
			log.error("Error while consuming login/logout event: {}", e.getMessage(), e);
		}

	}

	@KafkaListener(topics = KafkaTopics.RELAY_APPOINTMENT_EVENTS)
	public void encouterRelayToClinical(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment)
			throws Exception {
		acknowledgment.acknowledge();

		String value = consumerRecord.value();
		ObjectMapper objectMapper = new ObjectMapper();
		RelayEventDTO clinicalEvent = null;
		try {
			clinicalEvent = objectMapper.readValue(value, RelayEventDTO.class);

		} catch (JsonMappingException e1) {
		} catch (JsonProcessingException e1) {
		}

		if (clinicalEvent != null) {
			switch (clinicalEvent.getEventType()) {
			case ADD_HATI_APPOINTMENT:
				try {
					RelayBookAppointmentDto kafkaRequest = null;

					kafkaRequest = new ObjectMapper().readValue(clinicalEvent.getData(), RelayBookAppointmentDto.class);
					
					try {
						this.appointmentService.hatiBookNewAppointment(kafkaRequest);
					} catch (Exception e) {
						// TODO: handle exception
						MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
								AppUtil.convertJsonToString(kafkaRequest), 1, KafkaTopics.RELAY_APPOINTMENT_EVENTS,
								MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
								MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
						this.messageEventService.saveEvent(testEvent);
					}

				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
				}
				break;

			case APPOINTMENT_CANCEL_TOPIC:
				try {
					LtAppointmentCancelKafkaRequest kafkaRequest = null;

					kafkaRequest = new ObjectMapper().readValue(clinicalEvent.getData(),
							LtAppointmentCancelKafkaRequest.class);
					try {
						this.appointmentService.cancleApptntToRelay(kafkaRequest, "appt");
					} catch (Exception e) {
						// TODO: handle exception
						MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
								AppUtil.convertJsonToString(kafkaRequest), 1, KafkaTopics.RELAY_APPOINTMENT_EVENTS,
								MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
								MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
						this.messageEventService.saveEvent(testEvent);
					}

				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
				}
				break;

			case RELAY_RESCHEDULE_APPOINTMENT:
				try {
					LtRescheduleAppointmentDto kafkaRequest = null;

					kafkaRequest = new ObjectMapper().readValue(clinicalEvent.getData(),
							LtRescheduleAppointmentDto.class);

					try {
						this.appointmentService.rescheduleApptFromRelay(kafkaRequest);
					} catch (Exception e) {
						// TODO: handle exception
						MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
								AppUtil.convertJsonToString(kafkaRequest), 1, KafkaTopics.RELAY_APPOINTMENT_EVENTS,
								MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
								MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
						this.messageEventService.saveEvent(testEvent);
					}

				} catch (JsonMappingException e) {
				 e.printStackTrace();
				} catch (JsonProcessingException e) {
				}
				break;

			default:
				break;
			}
		}

	}

	@KafkaListener(topics = KafkaTopics.LT_OPD_EVENTS)
	public void updateOpStatus(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment)
			throws Exception {

		acknowledgment.acknowledge();
		String value = consumerRecord.value();
		KafkaOpdDto kafkaOpd = null;
		try {
			kafkaOpd = new ObjectMapper().readValue(value, KafkaOpdDto.class);
			switch (kafkaOpd.getRequestType()) {
			case NEED_CLOSE:

				NeedCloseUpdateDTO needClose;
				needClose = new ObjectMapper().readValue(kafkaOpd.getData(), NeedCloseUpdateDTO.class);
				try {
					this.appointmentService.updateJourneyStatus(needClose);
				} catch (Exception e) {
					// TODO: handle exception
					MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
							AppUtil.convertJsonToString(needClose), 1, KafkaTopics.LT_OPD_EVENTS,
							MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
							MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
					this.messageEventService.saveEvent(testEvent);
				}

				break;

			case CANCEL_VISIT:
				CancelVisitDTO cancelVisitDto = null;
				cancelVisitDto = this.objectMapper.readValue(kafkaOpd.getData(), CancelVisitDTO.class);
				try {
					this.appointmentService.cancelVisitforOPpatient(cancelVisitDto);
				} catch (Exception e) {
					// TODO: handle exception
					MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
							AppUtil.convertJsonToString(cancelVisitDto), 1, KafkaTopics.LT_OPD_EVENTS,
							MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
							MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
					this.messageEventService.saveEvent(testEvent);
				}
				break;

			default:
				break;
			}
		} catch (Exception e) {
//			this.messageEventService.saveEvent(consumerRecord.key(), KafkaTopics.LT_OPD_EVENTS,
//					MessageEventStatus.FAILED_ON_CONSUME,
//					kafkaOpd == null ? null : kafkaOpd.getTokenPayload() == null ? null : kafkaOpd.getTokenPayload(),
//					kafkaOpd, e.getMessage());
		}
	}

	@KafkaListener(topics = KafkaTopics.LT_UMS_DELETE_USER)
	public void deleteUser(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment)
			throws Exception {
		acknowledgment.acknowledge();
		String value = consumerRecord.value();
		DeleteUserDTO deleteUserDTO = null;
		try {
			deleteUserDTO = new ObjectMapper().readValue(value, DeleteUserDTO.class);
			this.appointmentSchedulerService.updateUserActive(deleteUserDTO.getUsername(), deleteUserDTO.getActive());
		} catch (Exception e1) {
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
					AppUtil.convertJsonToString(deleteUserDTO), 1, KafkaTopics.LT_UMS_DELETE_USER,
					MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CONSUME, e1.getMessage());
			this.messageEventService.saveEvent(testEvent);

		}

	}
	
	@KafkaListener(topics = { KafkaTopics.COMMITTEE_MEETING_SCHEDULE_PARTICIPANT_UPDATE })
	public void updateCommiteeMeetingSchedule(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment)
			throws Exception {
		acknowledgment.acknowledge();
		try {
			String value = consumerRecord.value();
			UpdateMeetingScheduledDto updateMeetingScheduledDto = null;
			updateMeetingScheduledDto = new ObjectMapper().readValue(value, UpdateMeetingScheduledDto.class);
				try {
					this.schedulerEventService.updateCommiteeMeetingSchedule(updateMeetingScheduledDto);
				} catch (ApplicationException e) {
					MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null,
							AppUtil.convertJsonToString(updateMeetingScheduledDto), 1, KafkaTopics.COMMITTEE_MEETING_SCHEDULE_PARTICIPANT_UPDATE,
							MessageRequestStatus.PENDING, this.getClass().getSimpleName(),
							MessageEventStatus.FAILED_ON_CONSUME, e.getMessage());
					this.messageEventService.saveEvent(testEvent);
				}

		} catch (Exception e) {
			throw new Exception("Failed to delete events");
		}
	}


}
