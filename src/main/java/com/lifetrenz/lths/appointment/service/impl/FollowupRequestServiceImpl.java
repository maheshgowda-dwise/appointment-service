package com.lifetrenz.lths.appointment.service.impl;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.common.util.KafkaTopics;
import com.lifetrenz.lths.appointment.dto.ClinicalEventDTO;
import com.lifetrenz.lths.appointment.dto.CustomerTransactionAttributeDTO;
import com.lifetrenz.lths.appointment.dto.FollowupRequestDto;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.enums.ClinicalEventType;
import com.lifetrenz.lths.appointment.mapper.EventsMapper;
import com.lifetrenz.lths.appointment.mapper.FollowupRequestMapper;
import com.lifetrenz.lths.appointment.model.collection.FollowupRequest;
import com.lifetrenz.lths.appointment.model.events.MessageEvent;
import com.lifetrenz.lths.appointment.repository.command.IFollowupRequestRepository;
import com.lifetrenz.lths.appointment.service.FollowupRequestService;
import com.lifetrenz.lths.appointment.service.ProducerService;
import com.lifetrenz.lths.appointment.util.AppUtil;

@Component
public class FollowupRequestServiceImpl implements FollowupRequestService {

	@Autowired
	IFollowupRequestRepository followupRequestRepository;

	@Autowired
	FollowupRequestMapper mapper;

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	EventsMapper eventsMapper;
	
	@Autowired
	ProducerService producerService;

	@Override
	public FollowupRequestDto creatFollowupRequest(FollowupRequestDto dto) {
		try {
			FollowupRequest entity = mapper.mapDtoToEntity(dto);
			FollowupRequest savedEntity = followupRequestRepository.save(entity);
			return mapper.mapEntityToDto(savedEntity);
		} catch (Exception e) {
			throw new RuntimeException("Error occurred while creating follow-up request", e);
		}
	}

	@Override
	public FollowupRequestDto findById(String id) {
		return followupRequestRepository.findById(id).map(mapper::mapEntityToDto)
				.orElseThrow(() -> new RuntimeException("Follow-up request not found with id: " + id));
	}

	@Override
	public List<FollowupRequestDto> getAllFollowupRequest() throws Exception {
		return followupRequestRepository.findAll().stream().map(mapper::mapEntityToDto).collect(Collectors.toList());
	}

	@Override
	public FollowupRequestDto updateFollowupRequest(FollowupRequestDto dto, TokenPayLoad tokenPayload) throws ApplicationException {
		FollowupRequest existingEntity = followupRequestRepository.findById(dto.getId())
				.orElseThrow(() -> new RuntimeException("Follow-up request not found with id: " + dto.getId()));

		existingEntity.setStatus(dto.getStatus());
		existingEntity.getCustomerTrasaction().setUpdatedById(dto.getCustomerTrasaction().getUpdatedById());
		existingEntity.getCustomerTrasaction().setUpdatedBy(dto.getCustomerTrasaction().getUpdatedBy());
		existingEntity.getCustomerTrasaction().setUpdatedOn(new Date());

		ClinicalEventDTO request = sendRefAptBookeStatus(dto);
		MessageEvent messageEvent = createMessageEvent(dto, request, tokenPayload);

		try {
			producerService.publishToKafka(1, KafkaTopics.CLINICAL_EVENTS, AppUtil.convertJsonToString(request),
					messageEvent);
		} catch (Exception e1) {
			throw new RuntimeException("Failed to publish to kafka", e1);
		}
		
		try {
			FollowupRequest updatedEntity = followupRequestRepository.save(existingEntity);
			return mapper.mapEntityToDto(updatedEntity);
		} catch (Exception e) {
			throw new RuntimeException("Failed to update follow-up request", e);
		}
		
	}

	@Override
	public Boolean deleteFollowupRequest(String id) throws Exception {
		if (followupRequestRepository.existsById(id)) {
			FollowupRequest existingEntity = followupRequestRepository.findById(id).get();
			existingEntity.getCustomerTrasaction().setActive(false);
			followupRequestRepository.save(existingEntity);
			return true;
		} else {
			return false;
		}

	}

	@Override
	public List<FollowupRequestDto> searchFollowupRequests(String userName, Long referredBy, Long referredTo, String patientNameMpi,
			Long fromDate, Long toDate, String status, String eventIdentifier, Long referredSite, String specialization,
			String priority, String site, String referralType) throws Exception {

		Query queryBooked = new Query();
//		if (!AppUtil.isNullString(userName)) {
//			queryBooked.addCriteria(Criteria.where("userName").is(userName));
//		}

		if (!AppUtil.isNullString(userName)) {
			queryBooked.addCriteria(Criteria.where("userName").regex(".*" + Pattern.quote(userName) + ".*", "i"));
		}

		if (referredBy != null) {
			queryBooked.addCriteria(Criteria.where("referredDocId").is(referredBy));
		}
		
		if (referredTo != null) {
			queryBooked.addCriteria(Criteria.where("userId").is(referredTo));
		}

		if (!AppUtil.isNullString(patientNameMpi)) {
			if (patientNameMpi.matches("\\d+")) {
				// It's all digits → Treat it as MPI
				queryBooked.addCriteria(Criteria.where("mpi").regex(".*" + Pattern.quote(patientNameMpi) + ".*", "i"));
			} else {
				queryBooked.addCriteria(
						Criteria.where("patientName").regex(".*" + Pattern.quote(patientNameMpi) + ".*", "i"));
			}
		}

		if (!AppUtil.isNullString(status)) {
			queryBooked.addCriteria(Criteria.where("status").is(status));
		}
		
		if (!AppUtil.isNullString(priority)) {
			queryBooked.addCriteria(Criteria.where("priority").is(priority));
		}
		
		if (!AppUtil.isNullString(site)) {
			queryBooked.addCriteria(Criteria.where("referralSiteName").is(site));
		}
		
		if (!AppUtil.isNullString(referralType)) {
			queryBooked.addCriteria(Criteria.where("referralTypes").is(referralType));
		}
		
		if (!AppUtil.isNullString(specialization)) {
		    queryBooked.addCriteria(
		        new Criteria().orOperator(
		            Criteria.where("specialisationIdentifier").is(specialization),
		            Criteria.where("referredSpecialisationIdentifier").is(specialization)
		        )
		    );
		}


		if (fromDate != null && toDate != null) {
			try {
//				queryBooked.addCriteria(Criteria.where("preferredDate").gte(fromDate).lte(toDate));
				
				Date from = new Date(fromDate);
		        Date to = new Date(toDate);

//		        queryBooked.addCriteria(
//		            Criteria.where("customerTrasaction.createdOn").gte(from).lte(to)
//		        );
				queryBooked.addCriteria(Criteria.where("customerTrasaction.createdOn").gte(new Date(fromDate)).lte(new Date(toDate)));
			} catch (Exception e) {
				throw new Exception("Error processing date range", e);
			}
		}


		if (!AppUtil.isNullString(eventIdentifier)) {
			queryBooked.addCriteria(Criteria.where("eventIdentifier").is(eventIdentifier));
		}

		if (referredSite != null) {
			queryBooked.addCriteria(Criteria.where("customerTrasaction.siteId").is(referredSite));
		}

		return this.mongoTemplate.find(queryBooked, FollowupRequest.class).stream().map(mapper::mapEntityToDto)
				.collect(Collectors.toList());

//		return followupRequestRepository
//				.findByCriteria(userName, patientName, fromDate, toDate,
//						AppUtil.isNullString(status) ? null : FollowupRequestStatus.valueOf(status))
//				.stream().map(mapper::mapEntityToDto).collect(Collectors.toList());
	}
	
	
	private ClinicalEventDTO sendRefAptBookeStatus(FollowupRequestDto dto) {
		FollowupRequestDto consultationDTO = new FollowupRequestDto();
		consultationDTO.setReferralId(dto.getReferralId());

		return new ClinicalEventDTO(ClinicalEventType.UPDATE_REF_APT_BOOK_STATUS,
				AppUtil.convertJsonToString(consultationDTO));
	}
	
	private MessageEvent createMessageEvent(FollowupRequestDto dto, ClinicalEventDTO request, TokenPayLoad tokenPayload) {
		
		CustomerTransactionAttributeDTO cta = new CustomerTransactionAttributeDTO(true, tokenPayload.getCoreUserId(),
				new Date(), null, null, tokenPayload.getCustomerBusinessId(), tokenPayload.getCustomerId(), null, null);
		
		return eventsMapper.convertToMessageEvent(AppUtil.convertJsonToString(request), 1,
				KafkaTopics.CLINICAL_EVENTS.toString(),cta,
				tokenPayload.getPreferred_username());
	}

}
