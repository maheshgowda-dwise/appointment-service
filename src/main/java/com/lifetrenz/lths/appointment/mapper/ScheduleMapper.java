package com.lifetrenz.lths.appointment.mapper;

import java.util.Date;

import com.lifetrenz.lths.appointment.dto.KafkaTransactionBase;
import com.lifetrenz.lths.appointment.dto.ParticipantScheduleGetDto;
import com.lifetrenz.lths.appointment.dto.ScheduledParticipantGetDTO;
import com.lifetrenz.lths.appointment.dto.UserUpdateProfileDTO;
import com.lifetrenz.lths.appointment.model.collection.ParticipantScheduleDetails;
import com.lifetrenz.lths.appointment.model.collection.ScheduledParticipant;
import com.lifetrenz.lths.appointment.model.collection.User;
import com.lifetrenz.lths.appointment.model.value_object.TransactionBase;

public class ScheduleMapper {

	public TransactionBase getTransactionBase(KafkaTransactionBase kafkaTransactionBase) {
		return new TransactionBase(kafkaTransactionBase.getActive(), kafkaTransactionBase.getCreatedBy(),
				kafkaTransactionBase.getCreatedById(), new Date(), kafkaTransactionBase.getUpdatedBy(),
				kafkaTransactionBase.getUpdatedById(), new Date());
	}

	public ScheduledParticipant getScheduledParticipant(ScheduledParticipant scheduledParticipant,
			UserUpdateProfileDTO updatedUser) {

		User participantUser = new User(scheduledParticipant.getParticipantUser().getId(), updatedUser.getAppLanguage(),
				updatedUser.getKnownLanguage(), scheduledParticipant.getParticipantUser().getCoreUserId(),
				updatedUser.getName(), updatedUser.getTelecom(), updatedUser.getEmail(),
				updatedUser.getUserQualifications(), updatedUser.getWorkExperiences(),
				updatedUser.getProfessionalStatement(), updatedUser.getPreference(), updatedUser.getGender(),
				updatedUser.getAddress() == null ? null
						: (updatedUser.getAddress().getNationality() == null ? null
								: getNationalityArray(updatedUser.getAddress().getNationality())),
				updatedUser.getAddress(), scheduledParticipant.getParticipantUser().getUsername(),
				scheduledParticipant.getParticipantUser().getPassword(), updatedUser.getSpecialties(),
				updatedUser.getSpecializationId(), updatedUser.getMedicalCouniclNo(), updatedUser.getSites(),
				getTransactionBase(updatedUser.getTransactionBase()),
				scheduledParticipant.getParticipantUser().getLocation(),
				scheduledParticipant.getParticipantUser().getIsLocalTime(),
				scheduledParticipant.getParticipantUser().getProfilePhoto(),
				scheduledParticipant.getParticipantUser().getSignatureUrl(),
				scheduledParticipant.getParticipantUser().getPortalEnable(),
				scheduledParticipant.getParticipantUser().getIsScheduled(),
				updatedUser.getDesignation(),updatedUser.getSalutationName(),
				updatedUser.getDepartment());

		scheduledParticipant.setParticipantUser(participantUser);

		return scheduledParticipant;
	}

	private String[] getNationalityArray(String nationalityName) {
		String[] response = new String[1];
		response[0] = nationalityName;
		return response;

	}
	
	public ScheduledParticipantGetDTO convertToScheduledParticipantGetDto(ScheduledParticipant scheduledParticipant) {
		
		if(scheduledParticipant != null) {
			ScheduledParticipantGetDTO scheduledParticipantGetDTO = new ScheduledParticipantGetDTO(scheduledParticipant.getId(),
					scheduledParticipant.getParticipantId(), scheduledParticipant.getConductingSiteId(), scheduledParticipant.getParticipantType(),
					scheduledParticipant.getCalendarType(), scheduledParticipant.getParticipantUser(), scheduledParticipant.getScheduleParticipantUser(),
					scheduledParticipant.getEquipment(), scheduledParticipant.getAmbulance(), scheduledParticipant.getLocation(),
					scheduledParticipant.getRole(), scheduledParticipant.getScheduleCount(), scheduledParticipant.getIsLogin(),
					scheduledParticipant.getConsultingLocation(), scheduledParticipant.getCustomerId(), scheduledParticipant.getCustomerBusinessId(),
					scheduledParticipant.getSiteId());
			return scheduledParticipantGetDTO;
		}
		return null;
	}
	
public ParticipantScheduleGetDto convertToParticipantScheduledGetDto(ParticipantScheduleDetails participantScheduleDetails) {
		
		if(participantScheduleDetails != null) {
			ParticipantScheduleGetDto participantScheduleGetDto = new ParticipantScheduleGetDto(participantScheduleDetails.getId(),
					participantScheduleDetails.getCalendarType(), participantScheduleDetails.getParticipantType(),
					participantScheduleDetails.getConductingSiteId(), participantScheduleDetails.getParticipantId(),
					participantScheduleDetails.getParticipantName(), participantScheduleDetails.getMaximumWaitingList(),
					participantScheduleDetails.getIsActive(), participantScheduleDetails.getDuration(),
					participantScheduleDetails.getScheduleConfig(), participantScheduleDetails.getSlotType(),
					participantScheduleDetails.getCustomScheduleDto(), participantScheduleDetails.getConfigBreak(),
					participantScheduleDetails.getScheduledParticipant(), participantScheduleDetails.getScheduleType(),
					participantScheduleDetails.getReason(), participantScheduleDetails.getMaxWaitingPerSlot(),
					participantScheduleDetails.getConsumedWaitingPerSession(), participantScheduleDetails.getConsumedWaitingPerSlot(),
					participantScheduleDetails.getBlockWaitingSession(), participantScheduleDetails.getBlockWaitingSlot(),
					participantScheduleDetails.getCustomerId(), participantScheduleDetails.getCustomerBusinessId(),
					participantScheduleDetails.getSiteId());
			return participantScheduleGetDto;
		}
		return null;
	}

}
