package com.lifetrenz.lths.appointment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.dto.FollowupRequestDto;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.model.collection.FollowupRequest;
import com.lifetrenz.lths.appointment.model.enums.FollowupRequestStatus;

@Service
public interface FollowupRequestService {

	/**
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	FollowupRequestDto creatFollowupRequest(FollowupRequestDto dto) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	FollowupRequestDto findById(String id) throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	List<FollowupRequestDto> getAllFollowupRequest() throws Exception;

	/**
	 * 
	 * @param id
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	FollowupRequestDto updateFollowupRequest(FollowupRequestDto dto, TokenPayLoad tokenPayload) throws ApplicationException;

	/**
	 * 
	 * @param id
	 * @throws Exception
	 */
	Boolean deleteFollowupRequest(String id) throws Exception;

	/**
	 * 
	 * @param userName
	 * @param referredBy
	 * @param patientNameMpi
	 * @param fromDate
	 * @param toDate
	 * @param status
	 * @param eventIdentifier
	 * @param referredSite
	 * @param specialization
	 * @param priority
	 * @return
	 * @throws Exception
	 */
	public List<FollowupRequestDto> searchFollowupRequests(String userName, Long referredBy, Long referredTo, String patientNameMpi,
			Long fromDate, Long toDate, String status, String eventIdentifier, Long referredSite, String specialization,
			String priority, String site, String referralType) throws Exception;

}
