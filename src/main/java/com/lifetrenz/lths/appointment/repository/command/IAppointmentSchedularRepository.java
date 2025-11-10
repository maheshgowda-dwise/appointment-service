package com.lifetrenz.lths.appointment.repository.command;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lifetrenz.lths.appointment.model.collection.ParticipantScheduleDetails;

public interface IAppointmentSchedularRepository extends MongoRepository<ParticipantScheduleDetails, String> {
	
	List<ParticipantScheduleDetails> findByParticipantId(String participantId) throws Exception;

}
