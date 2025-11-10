package com.lifetrenz.lths.appointment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.lifetrenz.lths.appointment.common.builders.KafkaProducerRecordBuilder;
import com.lifetrenz.lths.appointment.common.builders.MessageEventBuilder;
import com.lifetrenz.lths.appointment.common.util.AppUtils;
import com.lifetrenz.lths.appointment.mapper.EventsMapper;
import com.lifetrenz.lths.appointment.mapper.FollowupRequestMapper;
import com.lifetrenz.lths.appointment.mapper.ScheduleEventMapper;
import com.lifetrenz.lths.appointment.mapper.ScheduleMapper;

/**
 * 
 * @author Ajith.K
 *
 */
@org.springframework.context.annotation.Configuration
public class Configuration {

	@Bean
	public ValidatingMongoEventListener validatingMongoEventListener() {
		return new ValidatingMongoEventListener(validator());
	}

	@Bean
	public LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}

//	@Bean
//	public ModelMapper modelMapper() {
//		return new ModelMapper();
//	}

	@Bean
	public AppUtils appUtils() {
		return new AppUtils();
	}

	@Bean
	public MessageEventBuilder messageEventBuilderInstance() {
		return new MessageEventBuilder();
	}

	@Bean
	public KafkaProducerRecordBuilder kafkaProducerRecordBuilderInstance() {
		return new KafkaProducerRecordBuilder();
	}

	@Bean
	public ScheduleMapper mapper() {
		return new ScheduleMapper();
	}

	@Bean
	public EventsMapper eventsMapper() {
		return new EventsMapper();
	}

	@Bean
	public ScheduleEventMapper ScheduleEventMapper() {
		return new ScheduleEventMapper();
	}
	@Bean
	public FollowupRequestMapper followupRequestMapper() {
		return new FollowupRequestMapper();
	}
	
}
