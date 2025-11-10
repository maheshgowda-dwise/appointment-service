package com.lifetrenz.lths.appointment.repository.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.lifetrenz.lths.appointment.repository.command.IAppointmentSchedularRepository;

@Repository
public class AppointmentSchedularRepository {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	IAppointmentSchedularRepository iAppointmentSchedular;

}
