/**
 * 
 */
package com.lifetrenz.lths.appointment.repository.command;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lifetrenz.lths.appointment.model.collection.ScheduledParticipant;

/**
 * @author Ajith.K
 *
 */
public interface IScheduledParticipantRepository extends MongoRepository<ScheduledParticipant, String> {

	/**
	 * 
	 * @param coreUserId
	 * @return
	 */
	boolean existsByParticipantUser_CoreUserId(Long coreUserId);

	/**
	 * 
	 * @param participantId
	 * @return
	 */
	boolean existsByParticipantId(Long coreUserId);
	
	/**
	 * 
	 * @param coreUserId
	 * @param participantType
	 * @param siteId
	 * @param calendarType
	 * @return
	 */
	boolean existsByParticipantIdAndParticipantType_IdentifierCodeAndConductingSiteIdAndCalendarType_IdentifierCode(Long coreUserId, String participantType,Long siteId,String calendarType);

	/**
	 * 
	 * @param coreUserId
	 * @return
	 */
	List<ScheduledParticipant> findByParticipantId(Long coreUserId);


	/**
	 * 
	 * @param coreUserId
	 * @param participantType
	 * @param siteId
	 * @param calendarType
	 * @return
	 */
	
	ScheduledParticipant findByParticipantIdAndParticipantType_IdentifierCodeAndConductingSiteIdAndCalendarType_IdentifierCode(Long coreUserId, String participantType,Long siteId,String calendarType);

	/**
	 * 
	 * @param customerBusinessId
	 * @return
	 */
	List<ScheduledParticipant> findByCustomerBusinessId(Long customerBusinessId);

	/**
	 * 
	 * @param participantId
	 * @param siteId
	 * @return
	 */
	ScheduledParticipant findByParticipantIdAndConductingSiteId(Long participantId, Long siteId);

}
