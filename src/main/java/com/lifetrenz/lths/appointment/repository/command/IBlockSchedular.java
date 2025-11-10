package com.lifetrenz.lths.appointment.repository.command;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lifetrenz.lths.appointment.model.collection.BlockSchedular;

public interface IBlockSchedular extends MongoRepository<BlockSchedular, String> {
	
	List<BlockSchedular> findBySchedularEventId(String schedularEventId);

}
