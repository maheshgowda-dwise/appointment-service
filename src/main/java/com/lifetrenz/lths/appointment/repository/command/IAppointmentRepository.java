package com.lifetrenz.lths.appointment.repository.command;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lifetrenz.lths.appointment.model.collection.Appointment;

public interface IAppointmentRepository extends MongoRepository<Appointment, String> {

	List<Appointment> findByExternalAppointmentId(String externalAppointmentId);

}