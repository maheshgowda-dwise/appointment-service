package com.lifetrenz.lths.appointment.repository.command;

import org.springframework.data.repository.CrudRepository;

import com.lifetrenz.lths.appointment.model.collection.Slots;

public interface ISlotsRepository extends CrudRepository<Slots, String> {

}
