package com.lifetrenz.lths.appointment.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.common.app.exception.FailedException;
import com.lifetrenz.lths.appointment.common.util.KafkaTopics;
import com.lifetrenz.lths.appointment.dto.ClinicalEventDTO;
import com.lifetrenz.lths.appointment.dto.CustomerTransactionAttributeDTO;
import com.lifetrenz.lths.appointment.dto.ReferralConsultationDTO;
import com.lifetrenz.lths.appointment.enums.ClinicalEventType;
import com.lifetrenz.lths.appointment.enums.ReferralType;
import com.lifetrenz.lths.appointment.mapper.EventsMapper;
import com.lifetrenz.lths.appointment.model.collection.FollowupRequest;
import com.lifetrenz.lths.appointment.model.enums.FollowupRequestStatus;
import com.lifetrenz.lths.appointment.model.events.MessageEvent;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;
import com.lifetrenz.lths.appointment.repository.command.IFollowupRequestRepository;
import com.lifetrenz.lths.appointment.service.ProducerService;
import com.lifetrenz.lths.appointment.service.ReferralAptService;
import com.lifetrenz.lths.appointment.util.AppUtil;

/**
 *
 * @author Sai.KVSS
 *
 */
@Service
public class ReferralAptServiceImpl implements ReferralAptService {

	private final IFollowupRequestRepository followupRequestRepository;
	private final ProducerService producerService;
	private final EventsMapper eventsMapper;

	public ReferralAptServiceImpl(IFollowupRequestRepository followupRequestRepository, ProducerService producerService,
			EventsMapper eventsMapper) {
		this.followupRequestRepository = followupRequestRepository;
		this.producerService = producerService;
		this.eventsMapper = eventsMapper;
	}

	@Override
	public ReferralConsultationDTO saveReferralAppointments(ReferralConsultationDTO referralConsultationDTO)
			throws ApplicationException {

		try {
			FollowupRequest followupRequest = createFollowupRequest(referralConsultationDTO);
			followupRequestRepository.save(followupRequest);

			ClinicalEventDTO request = createClinicalEventRequest(referralConsultationDTO);
			MessageEvent messageEvent = createMessageEvent(referralConsultationDTO, request);

			producerService.publishToKafka(1, KafkaTopics.CLINICAL_EVENTS, AppUtil.convertJsonToString(request),
					messageEvent);
		} catch (Exception e) {
			throw new RuntimeException("Error saving referral appointments", e);
		}

		return referralConsultationDTO;
	}

	private FollowupRequest createFollowupRequest(ReferralConsultationDTO dto) {
		CustomerTransactionBase transactionBase = new CustomerTransactionBase();
		transactionBase.setCustomerId(dto.getTransAttribute().getCustomerId());
		transactionBase.setCustomerBusinessId(dto.getTransAttribute().getCustomerBusinessId());
		transactionBase.setSiteId(dto.getReferralSiteId());
		transactionBase.setActive(true);
		transactionBase.setCreatedBy(dto.getTransAttribute().getCreatedBy());
		transactionBase.setCreatedById(dto.getTransAttribute().getCreatedById());
		transactionBase.setCreatedOn(dto.getTransAttribute().getCreatedOn());

		
		String referralSiteName = null;

		if (dto.getReferralTypes() == ReferralType.INTER_REFERRAL) {
		    referralSiteName = dto.getReferredSiteName();
		} else if(dto.getReferralTypes() == ReferralType.INTRA_REFERRAL){
		    referralSiteName = dto.getReferralSiteName();
		}
		
		return new FollowupRequest(null, dto.getPhysicianName(),
				dto.getPhysicianId() != null ? Long.parseLong(dto.getPhysicianId()) : null,
				dto.getSpecialisation() != null ? dto.getSpecialisation().getNameEn() : null,
				dto.getPatient() != null ? dto.getPatient().getPatientName() : null,
				dto.getPatient() != null ? dto.getPatient().getPatientId() : null, dto.getSpecialisationIdentifier(),
				dto.getPatient() != null ? dto.getPatient().getMpi() : null,
				dto.getPreferedDate() != null ? dto.getPreferedDate().getTime() : null, dto.getReferralNotes(),
				FollowupRequestStatus.PENDING, transactionBase,
				dto.getEncounterId() != null ? Long.parseLong(dto.getEncounterId()) : null, dto.getAppointmentId(),
				dto.getIdentifier(), dto.getReferralId(), dto.getReferralTypes(), referralSiteName,
				dto.getReferralReason() != null ? dto.getReferralReason().getName() : null,
				dto.getPriority() != null ? dto.getPriority().getName() : null, dto.getGenderId(), dto.getGender(),
				dto.getDateOfBirth(), dto.getReferredDocname(), dto.getReferredDocId(),
				dto.getReferredDocSpecialisation(), dto.getReferredSpecialisationIdentifier());

	}

	private ClinicalEventDTO createClinicalEventRequest(ReferralConsultationDTO dto) {
		ReferralConsultationDTO consultationDTO = new ReferralConsultationDTO();
		consultationDTO.setReferralId(dto.getReferralId());

		return new ClinicalEventDTO(ClinicalEventType.UPDATE_REFERRAL_CONSULTATION_STATUS,
				AppUtil.convertJsonToString(consultationDTO));
	}

	private MessageEvent createMessageEvent(ReferralConsultationDTO dto, ClinicalEventDTO request) {
		return eventsMapper.convertToMessageEvent(AppUtil.convertJsonToString(request), 1,
				KafkaTopics.CLINICAL_EVENTS.toString(),
				new CustomerTransactionAttributeDTO(true, dto.getTransAttribute().getCreatedById(), new Date(), null,
						null, dto.getTransAttribute().getCustomerBusinessId(), dto.getTransAttribute().getCustomerId(),
						dto.getTransAttribute().getSiteId(), null),
				dto.getTransAttribute().getUpdatedBy());
	}

	@Override
	public Boolean deleteReferralAppointments(String referralId) throws ApplicationException {
		try {

			List<FollowupRequest> requests = followupRequestRepository.findByReferralId(referralId);
			boolean hasCompleted = requests.stream().anyMatch(r -> r.getStatus() == FollowupRequestStatus.COMPLETED);

			if (hasCompleted) {
				throw new FailedException("Cannot delete referral appointments with COMPLETED status");
			}

			followupRequestRepository.deleteByReferralId(referralId);
			return true;
		} catch (Exception e) {

			throw new RuntimeException("Error deleting referral appointments", e);
		}
	}

}
