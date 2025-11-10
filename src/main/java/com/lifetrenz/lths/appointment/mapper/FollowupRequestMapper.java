package com.lifetrenz.lths.appointment.mapper;

import com.lifetrenz.lths.appointment.dto.FollowupRequestDto;
import com.lifetrenz.lths.appointment.model.collection.FollowupRequest;

public class FollowupRequestMapper {

	public FollowupRequest mapDtoToEntity(FollowupRequestDto dto) {

		return new FollowupRequest(dto.getId(), dto.getUserName(), dto.getUserId(), null, dto.getPatientName(),
				dto.getPatientId(),null, dto.getMpi(), dto.getPreferredDate(), dto.getFollowupNote(), dto.getStatus(),
				dto.getCustomerTrasaction(), dto.getEncounterId(),null, "FOLLOW_UP", null, null, null, null, null, null,
				null, null, null, null, null,null);

	}

	public FollowupRequestDto mapEntityToDto(FollowupRequest dto) {

		return new FollowupRequestDto(dto.getId(), dto.getUserName(), dto.getUserId(), dto.getUserSpecialisation(),
				dto.getPatientName(), dto.getPatientId(),dto.getSpecialisationIdentifier(), dto.getMpi(), dto.getPreferredDate(), dto.getFollowupNote(),
				dto.getStatus(), dto.getCustomerTrasaction(), dto.getEncounterId(), dto.getAppointmentId(), dto.getEventIdentifier(),
				dto.getReferralId(), dto.getReferralTypes(), dto.getReferralSiteName(), dto.getReferralReason(),
				dto.getPriority(), dto.getGenderId(), dto.getGender(),
				dto.getDateOfBirth(), dto.getReferredDocname(), dto.getReferredDocId(),
				dto.getReferredDocSpecialisation(), dto.getReferredSpecialisationIdentifier());

	}

}
