package com.lifetrenz.lths.appointment.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifetrenz.lths.appointment.common.util.KafkaTopics;
import com.lifetrenz.lths.appointment.dto.ClinicalEventDTO;
import com.lifetrenz.lths.appointment.dto.ReferralConsultationDTO;

/**
 *
 * @author Sai.KVSS
 *
 */
@Service
public class IPDClinicalConsumerService {

	private final ReferralAptService referralAptService;
	private final ObjectMapper objectMapper;
	
	public IPDClinicalConsumerService(ReferralAptService referralAptService, ObjectMapper objectMapper) {
		this.referralAptService = referralAptService;
		this.objectMapper = objectMapper;
	}

	@KafkaListener(topics = KafkaTopics.CLINICAL_EVENTS)
	public void saveClinicalAdmissionDashboardListener(ConsumerRecord<Integer, String> consumerRecord,
			Acknowledgment acknowledgment) {
		try {
			acknowledgment.acknowledge();
			ClinicalEventDTO clinicalEvent = objectMapper.readValue(consumerRecord.value(), ClinicalEventDTO.class);

			if (clinicalEvent != null) {
				handleClinicalEvent(clinicalEvent);
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(this.getClass()).error("Failed to process clinical event: " + e.getMessage(), e);
		}
	}

	private void handleClinicalEvent(ClinicalEventDTO clinicalEvent) {
		try {
			switch (clinicalEvent.getEventType()) {
				case PATIENT_REFERRAL_CONSULTATION:
					processReferralConsultation(clinicalEvent);
					break;
				case DELETE_REFERRAL_CONSULTATION:
					deleteReferralConsultation(clinicalEvent);
					break;
				default:
					LoggerFactory.getLogger(this.getClass()).warn("Unhandled event type: " + clinicalEvent.getEventType());
					break;
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(this.getClass()).error("Error handling clinical event: " + e.getMessage(), e);
		}
	}

	private void processReferralConsultation(ClinicalEventDTO clinicalEvent) throws Exception {
		ReferralConsultationDTO referralConsultationDTO = objectMapper.readValue(clinicalEvent.getData(),
				ReferralConsultationDTO.class);
		referralAptService.saveReferralAppointments(referralConsultationDTO);
	}

	private void deleteReferralConsultation(ClinicalEventDTO clinicalEvent) throws Exception {
		ReferralConsultationDTO referralConsultationDTO = objectMapper.readValue(clinicalEvent.getData(),
				ReferralConsultationDTO.class);
		referralAptService.deleteReferralAppointments(referralConsultationDTO.getReferralId());
	}
}
