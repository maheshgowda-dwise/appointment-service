/**
 * 
 */
package com.lifetrenz.lths.appointment.repository.command;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.lifetrenz.lths.appointment.model.collection.SchedulerEvent;

/**
 * @author Ajith.K
 *
 */
public interface ISchedulerEventRepository extends MongoRepository<SchedulerEvent, String> {
	
	List<SchedulerEvent> findByReferrenceId(String referrenceId);
	
	List<SchedulerEvent> findByEventData_AppointmentId(String appointmentId);
	
	List<SchedulerEvent> findByEventData_Id(Long id);
	
	List<SchedulerEvent> findByReferrenceIdAndParticipant_ParticipantId(String referrenceId, String participantId);
	
	List<SchedulerEvent> findByEventData_IdAndEventDataIsBlock(Long id,Boolean isBlock);
	
	
	@Query("{ 'eventData.startTime': { $gte: ?0, $lt: ?1 }, 'conductingSiteId': ?2, 'eventData.isAllDay': true }")
	List<SchedulerEvent> findByStartTimeBetweenAndSiteAndIsAllDayTrue(Date startOfDay, Date endOfDay, Long siteId);

	List<SchedulerEvent> findByParticipant_ParticipantId(String participantId);



}
