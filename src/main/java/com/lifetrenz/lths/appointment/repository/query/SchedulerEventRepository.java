package com.lifetrenz.lths.appointment.repository.query;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.lifetrenz.lths.appointment.model.collection.SchedulerEvent;

@Repository
public class SchedulerEventRepository {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	public List<SchedulerEvent> getEventsByRefferenceIdAndException(Long recurrenceId,String exception){
		

		Query query = new Query();
		
		query.addCriteria(
			    Criteria.where("eventData._id").is(recurrenceId)
			           .and("eventData.recurrenceException").regex("(^|,)" + Pattern.quote(exception) + "($|,)")
			);
		

		List<SchedulerEvent> results = mongoTemplate.find(query, SchedulerEvent.class);
		
		return results;

		
	}

}
