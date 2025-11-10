package com.lifetrenz.lths.appointment.repository.query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.common.app.exception.FailedException;
import com.lifetrenz.lths.appointment.common.app.exception.NotFoundException;
import com.lifetrenz.lths.appointment.common.util.AppUtils;
import com.lifetrenz.lths.appointment.dto.OldAppointmentDto;
import com.lifetrenz.lths.appointment.mapper.AppointmentMapper;
import com.lifetrenz.lths.appointment.model.collection.Appointment;
import com.lifetrenz.lths.appointment.model.collection.ScheduledParticipant;
import com.lifetrenz.lths.appointment.model.events.MessageEvent;
import com.lifetrenz.lths.appointment.model.value_object.ParticipantDetails;
//gitlab.lifetrenz.com/lifetrenz/lths-v2/hati-be/appointment-service.git
import com.lifetrenz.lths.appointment.repository.command.IAppointmentRepository;

/**
 * 
 * @author Mujaheed.N
 *
 */
@Repository
public class AppointmentRepository {

    private static final Logger log = LoggerFactory.getLogger(AppointmentRepository.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    IAppointmentRepository appointmentRepo;

    @Autowired
    AppointmentMapper appointmentMapper;

    public List<OldAppointmentDto> getAppointmentSlots(String doctorId) throws FailedException {
        try {
            Query query = buildQueryForDoctorId(doctorId);
            return mongoTemplate.find(query, Appointment.class).stream()
                    .map(appointmentMapper::mapToAppointmentDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new FailedException("Failed to fetch appointment slots for doctorId: " + doctorId, e);
        }
    }

    public List<OldAppointmentDto> getAppointmentSlots(Long customerBusinessId, Long customerId, Long siteId,
            String speciality, Long startDate, Long endDate, String participantId, String participantType,
            String serviceType) throws FailedException {
        try {
            Query query = buildQueryForAppointmentSlots(customerBusinessId, customerId, siteId, startDate, endDate,
                    participantId, participantType, serviceType);
            List<Appointment> appointments = mongoTemplate.find(query, Appointment.class);
            return appointments.stream()
                    .map(appointmentMapper::mapToAppointmentDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new FailedException("Failed to fetch appointment slots", e);
        }
    }

    public List<Appointment> getAvailabiltySlots(Long customerBusinessId, Long customerId, Long siteId, Long startDate,
            Long endDate, String participantId, String participantType) throws FailedException {
        try {
            Query query = buildQueryForAppointmentSlots(customerBusinessId, customerId, siteId, startDate, endDate,
                    participantId, participantType, null);
            return mongoTemplate.find(query, Appointment.class);
        } catch (Exception e) {
            throw new FailedException("Failed to fetch availability slots", e);
        }
    }

    private Query buildQueryForDoctorId(String doctorId) {
        Query query = new Query();
        if (doctorId != null) {
            query.addCriteria(Criteria.where("particpantCalendar.participantId").is(doctorId));
        }
        return query;
    }

    private Query buildQueryForAppointmentSlots(Long customerBusinessId, Long customerId, Long siteId, Long startDate,
            Long endDate, String participantId, String participantType, String serviceType) {
        Query query = new Query();

        if (customerBusinessId != null && customerId != null) {
            query.addCriteria(Criteria.where("transactionBase.customerBusinessId").is(customerBusinessId)
                    .and("transactionBase.customerId").is(customerId));
        }
        if (startDate != null) {
            query.addCriteria(Criteria.where("startDateEpoc").gte(startDate));
        }
        if (endDate != null) {
            query.addCriteria(Criteria.where("endDateEpoc").lte(endDate));
        }
        if (!AppUtils.isNullString(participantId)) {
            query.addCriteria(Criteria.where("particpantCalendar.participantId").is(participantId));
        }
        if (!AppUtils.isNullString(participantType)) {
            query.addCriteria(Criteria.where("particpantCalendar.appointmentParticipantType.identifierCode")
                    .is(participantType));
        }
        if (!AppUtils.isNullString(serviceType)) {
            query.addCriteria(Criteria.where("appointmentCategory.identifierCode").is(serviceType));
        }
        query.addCriteria(Criteria.where("appointmentStatus.identifierCode").ne("Cancel"));

        return query;
    }

	// Removed duplicate getAppointmentSlots method to resolve compilation error.
	
	// public List<Appointment> getAvailabiltySlots(Long customerBusinessId, Long customerId, Long siteId,
	// 		 Long startDate, Long endDate, String participantId, String participantType) {
	// 	List<Appointment> getAllApptSlots = null;

	// 	Query query = new Query();
	// 	try {

	// 		if (customerBusinessId != null || customerId != null) {
	// 			query.addCriteria(Criteria.where("transactionBase.customerBusinessId").is(customerBusinessId)
	// 					.and("transactionBase.customerId").is(customerId));
	// 		}
		
	// 		if (startDate != null) {
	// 			query.addCriteria(Criteria.where("startDateEpoc").gte((startDate)));
	// 		}
	// 		if (endDate != null) {
	// 			query.addCriteria(Criteria.where("endDateEpoc").lte((endDate)));
	// 		}
	// 		if (!AppUtils.isNullString(participantId)) {
	// 			query.addCriteria(Criteria.where("particpantCalendar.participantId").is(participantId));
	// 		}
	// 		if (!AppUtils.isNullString(participantType)) {
	// 			query.addCriteria(Criteria.where("particpantCalendar.appointmentParticipantType.identifierCode")
	// 					.is(participantType));
	// 		}

	// 		query.addCriteria(Criteria.where("appointmentStatus.identifierCode").ne("Cancel"));
	// 		getAllApptSlots = mongoTemplate.find(query, Appointment.class);

	// 	} catch (Exception e) {

	// 	}
	// 	return getAllApptSlots;
	// }

	public List<OldAppointmentDto> getPatientAppointments(String patientId, String startDate, String endDate)throws FailedException {

		Query query = new Query();

		try {
			if (patientId != null) {
				query.addCriteria(Criteria.where("particpantCalendar.participantId").is(patientId));
			}

			if (startDate != null) {
				// query.addCriteria(Criteria.where("appointmentStartDate").gte((startDate)));
				query.addCriteria(Criteria.where("startDateEpoc").gte(startDate));
			}
			if (endDate != null) {
				// query.addCriteria(Criteria.where("appointmentEndDate").lte((endDate)));
				query.addCriteria(Criteria.where("endDateEpoc").lte(endDate));
			}

			List<Appointment> appointments = this.mongoTemplate.find(query, Appointment.class);
			
			return appointments.stream().map(app -> appointmentMapper.mapToAppointmentDto(app)).collect(Collectors.toList());

		} catch (Exception e) {
			throw new FailedException(e.getLocalizedMessage());
		}
	}
	
	public List<Appointment> getDoctorAppointments(String doctorId, String startDate, String endDate,Long siteId) {
		List<Appointment> getDoctorAppointments = null;
		Query query = new Query();

		try {
			if (doctorId != null) {
				query.addCriteria(Criteria.where("particpantCalendar.participantId").is(doctorId));
			}

			if (startDate != null) {
				// query.addCriteria(Criteria.where("appointmentStartDate").gte((startDate)));
				query.addCriteria(Criteria.where("startDateEpoc").gte(Long.parseLong(startDate)));
			}
			if (endDate != null) {
				// query.addCriteria(Criteria.where("appointmentEndDate").lte((endDate)));
				query.addCriteria(Criteria.where("endDateEpoc").lte(Long.parseLong(endDate)));
			}
			
			if(siteId != null) {
				query.addCriteria(Criteria.where("appointmentSite.id").is(siteId));
			}

			getDoctorAppointments = this.mongoTemplate.find(query, Appointment.class);

		} catch (Exception e) {

		}
		return getDoctorAppointments;
	}

	public List<Appointment> getNonAvab(Long siteId, Long startDate, Long endDate, String doctorId, String patient,
			String participantType, String visitType, String appiointmentStatus, String participant) throws FailedException {
		List<Appointment> getNon = null;

		Query query = new Query();

		if (appiointmentStatus != null) {
			query.addCriteria(Criteria.where("appointmentStatus.identifierCode").is(appiointmentStatus));
		}

		if (siteId != null) {
			query.addCriteria(Criteria.where("appointmentSite._id").is(siteId));
		}
		if (doctorId != null && !AppUtils.isNullString(doctorId)) {
			query.addCriteria(Criteria.where("particpantCalendar.participantId").is(doctorId));
		}

		if (startDate != null && startDate != 0) {
			query.addCriteria(Criteria.where("startDateEpoc").gte(startDate));
		}
		if (endDate != null && endDate != 0) {
			query.addCriteria(Criteria.where("endDateEpoc").lte(endDate));
		}
		if (patient != null && !AppUtils.isNullString(patient)) {
//			query.addCriteria(Criteria.where("particpantCalendar.1.participantName").is(patient));
			query.addCriteria(Criteria.where("particpantCalendar.1.participantName").regex(Pattern.compile(patient,Pattern.CASE_INSENSITIVE)));

			
		
		}
		
		
		if(participant != null && !AppUtils.isNullString(participant)) {
			query.addCriteria(Criteria.where("particpantCalendar.0.participantName").is(participant));
		}
		if (participantType != null && !AppUtils.isNullString(participantType)) {
			query.addCriteria(
					Criteria.where("particpantCalendar.appointmentParticipantType.identifierCode").is(participantType));
		}
		if (visitType != null && !AppUtils.isNullString(visitType)) {  
			query.addCriteria(Criteria.where("visitType.identifierCode").is(visitType));
		}
		
		query.with(Sort.by(Sort.Direction.DESC, "startDate"));

		getNon = this.mongoTemplate.find(query, Appointment.class);

		return getNon;

	}

	public List<Appointment> getscheduledParticipantConfig(ScheduledParticipant scheduledParticipant) throws Exception {
		List<Appointment> getNon = null;

		Query query = new Query();

		if (scheduledParticipant != null) {
			query.addCriteria(Criteria.where("scheduledParticipant").is(scheduledParticipant));
		}

		getNon = this.mongoTemplate.find(query, Appointment.class);

		return getNon;

	}

	public List<Appointment> getPatientAppointment(String patientId, Long siteId, String doctorSpecialityIdentifier,
			String doctorId, Long startDate, Long endDate, String visitType, String appointmentMode) {

		List<Appointment> dbResults = null;

		Query query = new Query();

		try {
			if (patientId != null || siteId != null) {

				query.addCriteria(new Criteria().andOperator(
						Criteria.where("particpantCalendar.participantId").is(patientId),
						Criteria.where("particpantCalendar.appointmentParticipantType.identifierCode").is("Patient")));

				query.addCriteria(Criteria.where("appointmentSite.id").is(siteId));
			}
			if (!AppUtils.isNullString(doctorSpecialityIdentifier)) {
				query.addCriteria(Criteria.where("particpantCalendar.doctorDetails.specialties.identifierCode")
						.is(doctorSpecialityIdentifier));
			}
			if (patientId == null && !AppUtils.isNullString(doctorId)) {
				try {
					query.addCriteria(
							new Criteria().andOperator(Criteria.where("particpantCalendar.participantId").is(doctorId),
									Criteria.where("particpantCalendar.appointmentParticipantType.identifierCode")
											.is("Practitioner")));
				} catch (Exception e) {
				}

			}
			if (startDate != null) {
				query.addCriteria(Criteria.where("startDateEpoc").gte((startDate)));
			}
			if (endDate != null) {
				query.addCriteria(Criteria.where("endDateEpoc").lte((endDate)));
			}
			if (!AppUtils.isNullString(visitType)) {
				query.addCriteria(Criteria.where("visitType.identifierCode").is(visitType));
			}
			if (!AppUtils.isNullString(appointmentMode)) {
				query.addCriteria(Criteria.where("appointmentConductMode.identifierCode").is(appointmentMode));
			}
			
			query.addCriteria(Criteria.where("appointmentStatus.identifierCode").ne("Cancel"));
			dbResults = mongoTemplate.find(query, Appointment.class);

			if (dbResults != null && dbResults.size() > 0) {
				List<Appointment> getAllPreviousSlots = new ArrayList<>();
				if (!AppUtils.isNullString(doctorId)) {
					for (Appointment appointment : dbResults) {
						for (ParticipantDetails participantDetails : appointment.getParticpantCalendar()) {
							if (participantDetails.getAppointmentParticipantType().getIdentifierCode()
									.equals("Practitioner") && participantDetails.getParticipantId().equals(doctorId)) {
								getAllPreviousSlots.add(appointment);
							}
						}
					}
					return getAllPreviousSlots;
				} else {
					return dbResults;
				}

			}
		} catch (Exception e) {
		}
		return null;
	}

	public List<Appointment> getMobPatientAppointment(String patientId, Long startDate, Long endDate,
			String appStatus) {
		List<Appointment> getAllAppointment = null;

		Query query = new Query();

		try {
			if (patientId != null) {
				query.addCriteria(Criteria.where("particpantCalendar.participantId").is(patientId));
			}

			if (startDate != null) {
				query.addCriteria(Criteria.where("startDateEpoc").gte(startDate));
			}
			if (endDate != null) {
				query.addCriteria(Criteria.where("endDateEpoc").lte(endDate));
			}
			if (appStatus != null && !appStatus.equals("")) {
				query.addCriteria(Criteria.where("appointmentStatus.identifierCode").is(appStatus));
			} else {
				query.addCriteria(Criteria.where("appointmentStatus.identifierCode").ne("Cancel"));
			}

			// query.addCriteria(Criteria.where("appointmentStatus").ne("CANCELLED"));
			getAllAppointment = mongoTemplate.find(query, Appointment.class);
		} catch (Exception e) {
		}
		return getAllAppointment;

	}

//	call sender appointment
	
	public List<Appointment> getCallCenterAppointmentSlots(Long customerBusinessId, Long customerId, Long siteId,
			String speciality, Long startDate, Long endDate, String participantId, String participantType,
			String serviceType,String payerType,String appoappointmentStatus,String appointmentCategory,
			String Number,String appointmentConductMode,String patientName,String visitType,String role) {
		List<Appointment> getCallCenterAppointmentSlots = null;

		Query query = new Query();
		try {

			if (customerBusinessId != null || customerId != null) {
				query.addCriteria(Criteria.where("transactionBase.customerBusinessId").is(customerBusinessId)
						.and("transactionBase.customerId").is(customerId));
			}
			 if (siteId != null) {
			 query.addCriteria(Criteria.where("appointmentSite._id").is(siteId));
			 }
			if (startDate != null) {
				query.addCriteria(Criteria.where("startDateEpoc").gte((startDate)));
			}
			if (endDate != null) {
				query.addCriteria(Criteria.where("endDateEpoc").lte((endDate)));
			}
			if (!AppUtils.isNullString(participantId)) {
				query.addCriteria(Criteria.where("particpantCalendar.0.participantId")
						.is(participantId));
			}
			if (!AppUtils.isNullString(participantType)) {
				query.addCriteria(Criteria.where("particpantCalendar.0.appointmentParticipantType.identifierCode")
						.is(participantType));
			}
			if (!AppUtils.isNullString(appoappointmentStatus)) {
				query.addCriteria(Criteria.where("appointmentStatus.identifierCode")
						.is(appoappointmentStatus));
			}
			if (!AppUtils.isNullString(appointmentCategory)) {
				query.addCriteria(Criteria.where("appointmentCategory.identifierCode")
						.is(appointmentCategory));
			}

			if (!AppUtils.isNullString(payerType)) {
				query.addCriteria(Criteria.where("payerType.identifierCode")
						.is(payerType));
			}
			if (!AppUtils.isNullString(Number)) {
				query.addCriteria(Criteria.where("particpantCalendar.1.patientDetails.telecom.number")
						.is(Number));
			}
			
			if (!AppUtils.isNullString(appointmentConductMode)) {
				query.addCriteria(Criteria.where("appointmentConductMode.identifierCode")
						.is(appointmentConductMode));
			}
			
			if (!AppUtils.isNullString(visitType)) {
				query.addCriteria(Criteria.where("visitType.identifierCode")
						.is(visitType));
			}
		
			if (!AppUtils.isNullString(role)) {
				query.addCriteria(Criteria.where("particpantCalendar.doctorDetails.role.identifier")
						.is(role));
			}
		
			if(!AppUtils.isNullString(patientName)) {
				query.addCriteria(Criteria.where("particpantCalendar.1.participantName").regex(Pattern.compile(patientName,Pattern.CASE_INSENSITIVE)));
			}

			
			if (!AppUtils.isNullString(serviceType)) {
				query.addCriteria(Criteria.where("appointmentCategory.identifierCode").is(serviceType));
			}
//			query.addCriteria(Criteria.where("appointmentStatus.identifierCode").ne("Cancel"));

			getCallCenterAppointmentSlots = mongoTemplate.find(query, Appointment.class);

		} catch (Exception e) {

		}
		return getCallCenterAppointmentSlots;
	}
	
	//ReConfirm call center
	
	public List<Appointment> getCallcenterReconfirmAppointment(Long customerBusinessId, Long customerId, Long siteId,
			String speciality, Long startDate, Long endDate, String participantId, String participantType,
			String serviceType,String payerType,String appoappointmentStatus,String appointmentCategory,
			String Number,String appointmentConductMode,String patientName,String visitType,String role) {
		List<Appointment> getCallCenterAppointmentSlots = null;

		Query query = new Query();
		try {

			if (customerBusinessId != null || customerId != null) {
				query.addCriteria(Criteria.where("transactionBase.customerBusinessId").is(customerBusinessId)
						.and("transactionBase.customerId").is(customerId));
			}
			 if (siteId != null) {
			 query.addCriteria(Criteria.where("appointmentSite._id").is(siteId));
			 }
			if (startDate != null) {
				query.addCriteria(Criteria.where("startDateEpoc").gte((startDate)));
			}
			if (endDate != null) {
				query.addCriteria(Criteria.where("endDateEpoc").lte((endDate)));
			}
			if (!AppUtils.isNullString(participantId)) {
				query.addCriteria(Criteria.where("particpantCalendar.0.participantId")
						.is(participantId));
			}
			if (!AppUtils.isNullString(participantType)) {
				query.addCriteria(Criteria.where("particpantCalendar.0.appointmentParticipantType.identifierCode")
						.is(participantType));
			}
			if (!AppUtils.isNullString(appoappointmentStatus)) {
				query.addCriteria(Criteria.where("appointmentStatus.identifierCode")
						.is(appoappointmentStatus));
			}
			if (!AppUtils.isNullString(appointmentCategory)) {
				query.addCriteria(Criteria.where("appointmentCategory.identifierCode")
						.is(appointmentCategory));
			}

			if (!AppUtils.isNullString(payerType)) {
				query.addCriteria(Criteria.where("payerType.identifierCode")
						.is(payerType));
			}
			if (!AppUtils.isNullString(Number)) {
				query.addCriteria(Criteria.where("particpantCalendar.1.patientDetails.telecom.number")
						.is(Number));
			}
			
			if (!AppUtils.isNullString(appointmentConductMode)) {
				query.addCriteria(Criteria.where("appointmentConductMode.identifierCode")
						.is(appointmentConductMode));
			}
			
			if (!AppUtils.isNullString(visitType)) {
				query.addCriteria(Criteria.where("visitType.identifierCode")
						.is(visitType));
			}
		
			if (!AppUtils.isNullString(role)) {
				query.addCriteria(Criteria.where("particpantCalendar.doctorDetails.role.identifier")
						.is(role));
			}
		
			if(!AppUtils.isNullString(patientName)) {
				query.addCriteria(Criteria.where("particpantCalendar.1.participantName").regex(Pattern.compile(patientName,Pattern.CASE_INSENSITIVE)));
			}

			
			if (!AppUtils.isNullString(serviceType)) {
				query.addCriteria(Criteria.where("appointmentCategory.identifierCode").is(serviceType));
			}
			query.addCriteria(Criteria.where("reconfirmedReason").is(""));
//			query.addCriteria(Criteria.where("appointmentStatus.identifierCode").ne("Cancel"));

			getCallCenterAppointmentSlots = mongoTemplate.find(query, Appointment.class);

		} catch (Exception e) {

		}
		return getCallCenterAppointmentSlots;
	}
	
	public List<MessageEvent> getDetailedFailedEvents(Long startDate, Long endDate, String serviceName,
			String topicName){
		List<MessageEvent> msgEvt = null;
		Query query = new Query();
		
		if(serviceName.equals("Appointment Service")) {
			if (startDate != null && endDate != null) {
			query.addCriteria(Criteria.where("transactionBase.createdOn").gte(new Date(startDate)).lt
					(new Date(endDate)));
			}
			
//			query.addCriteria(Criteria.where("eventStatus").is("FAILURE"));
			
	        if (topicName != "" && topicName.length() >= 3) {
	        	
	            query.addCriteria(Criteria.where("topic").regex("^" + topicName,"i"));
	        }
		}
		
		 msgEvt = mongoTemplate.find(query, MessageEvent.class);
		 return msgEvt;
	}
	
	
	public List<Appointment> getAppointmentDetails(Long createdOn) {
		
		Date fromDate = new Date(createdOn);
	    Calendar calendarFrom = Calendar.getInstance();
	    calendarFrom.setTime(fromDate);
	    calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
	    calendarFrom.set(Calendar.MINUTE, 0);
	    calendarFrom.set(Calendar.SECOND, 0);
	    calendarFrom.set(Calendar.MILLISECOND, 0);
	    long startOfDayEpoch = calendarFrom.getTimeInMillis();

	    Calendar calendarTo = Calendar.getInstance();
	    calendarTo.setTime(fromDate);
	    calendarTo.set(Calendar.HOUR_OF_DAY, 23);
	    calendarTo.set(Calendar.MINUTE, 59);
	    calendarTo.set(Calendar.SECOND, 59);
	    calendarTo.set(Calendar.MILLISECOND, 999);
	    long endOfDayEpoch = calendarTo.getTimeInMillis();

	    Query query = new Query();
	    query.addCriteria(Criteria.where("startDateEpoc").gte(startOfDayEpoch).lte(endOfDayEpoch));

	    List<Appointment> response = mongoTemplate.find(query, Appointment.class);
	    return response;
		
	}

	/**
	 * Check if an appointment already exists for the given criteria (optimized for performance)
	 * @param customerId Customer ID from transaction base
	 * @param slotId Slot ID to check for booking
	 * @param startDate Start date in epoch format
	 * @return List of existing appointments matching the criteria
	 * @throws FailedException if database query fails
	 */
	public List<Appointment> findExistingAppointmentBySlot(Long customerId, String slotId, Long startDate) 
			throws FailedException {
		try {
			// Reduced logging for performance - only essential logs
			if (log.isDebugEnabled()) {
				log.debug("DB query for slot validation - customerId: {}, slotId: {}", customerId, slotId);
			}
			
			// Optimized query building - more efficient criteria construction
			List<Criteria> criteriaList = new ArrayList<>();
			
			if (customerId != null) {
				criteriaList.add(Criteria.where("transactionBase.customerId").is(customerId));
			}
			
			if (!AppUtils.isNullString(slotId)) {
				criteriaList.add(Criteria.where("slotId").is(slotId));
			}
			
			if (startDate != null) {
				criteriaList.add(Criteria.where("startDateEpoc").is(startDate));
			}
			
			// Combine exclusion criteria for better performance
			criteriaList.add(Criteria.where("isWaitingList").ne(true));
			criteriaList.add(Criteria.where("appointmentStatus.identifierCode").ne("Cancel"));
			
			Query query = new Query();
			// Use andOperator for better query performance
			query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
			
			// Optimize fields returned for better performance - only fetch what we need
			query.fields().include("_id", "appointmentStatus.identifierCode", 
								  "transactionBase.customerId", "slotId", "startDateEpoc");
			
			// Limit results since we only care if any exist
			query.limit(1);
			
			List<Appointment> existingAppointments = mongoTemplate.find(query, Appointment.class);
			
			if (!existingAppointments.isEmpty() && log.isWarnEnabled()) {
				log.warn("Slot conflict detected - customerId: {}, slotId: {}", customerId, slotId);
			}
			
			return existingAppointments;
			
		} catch (Exception e) {
			log.error("Slot validation query failed - customerId: {}, slotId: {}, startDate: {}", 
				customerId, slotId, startDate, e);
			throw new FailedException("Database query failed while checking slot availability: " + e.getMessage(), e);
		}
	}

}
