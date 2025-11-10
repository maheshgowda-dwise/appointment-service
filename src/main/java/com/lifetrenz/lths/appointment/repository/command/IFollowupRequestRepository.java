package com.lifetrenz.lths.appointment.repository.command;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.lifetrenz.lths.appointment.model.collection.FollowupRequest;
import com.lifetrenz.lths.appointment.model.enums.FollowupRequestStatus;

public interface IFollowupRequestRepository extends MongoRepository<FollowupRequest, String> {

	// Combined search with multiple optional parameters including date range
	@Query("{$and: [" + "?0 == null ? {} : {userName: ?0}," + "?1 == null ? {} : {patientName: ?1},"
			+ "?2 == null && ?3 == null ? {} : {preferredDate: {$gte: ?2, $lte: ?3}},"
			+ "?4 == null ? {} : {status: ?4}" + "]}")
	List<FollowupRequest> findByCriteria(String userName, String patientName, Long fromDate, Long toDate,
			FollowupRequestStatus status);
	
	public void deleteByReferralId(String referralId);
	
	List<FollowupRequest> findByReferralId(String referralId);

}
