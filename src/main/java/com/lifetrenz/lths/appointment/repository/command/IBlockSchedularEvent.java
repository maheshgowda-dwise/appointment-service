package com.lifetrenz.lths.appointment.repository.command;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lifetrenz.lths.appointment.model.collection.BlockSchedularEvent;

public interface IBlockSchedularEvent extends MongoRepository<BlockSchedularEvent, String> {
	List<BlockSchedularEvent> findBySchedularEventId(String schedularEventId);

}
