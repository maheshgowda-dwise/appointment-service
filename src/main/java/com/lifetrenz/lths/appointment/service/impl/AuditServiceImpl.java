package com.lifetrenz.lths.appointment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.common.app.exception.FailedException;
import com.lifetrenz.lths.appointment.common.enums.MessageEventStatus;
import com.lifetrenz.lths.appointment.common.enums.MessageRequestStatus;
import com.lifetrenz.lths.appointment.common.util.KafkaTopics;
import com.lifetrenz.lths.appointment.dto.CustomerTransactionAttributeDTO;
import com.lifetrenz.lths.appointment.dto.LtAuditEventDto;
import com.lifetrenz.lths.appointment.mapper.EventsMapper;
import com.lifetrenz.lths.appointment.model.events.MessageEvent;
import com.lifetrenz.lths.appointment.service.AuditService;
import com.lifetrenz.lths.appointment.service.MessageEventService;
import com.lifetrenz.lths.appointment.service.ProducerService;
import com.lifetrenz.lths.appointment.util.AppUtil;

@Service
public class AuditServiceImpl implements AuditService {

	@Autowired
	ProducerService producerService;

	@Autowired
	EventsMapper eventsMapper;

	@Autowired
	MessageEventService messageEventService;

	@Async
	public void addtoAudit(LtAuditEventDto request, CustomerTransactionAttributeDTO custAttr, String path)
			throws ApplicationException {
		MessageEvent messageEvent = null;
		try {
			messageEvent = new MessageEvent(null, custAttr, 1, KafkaTopics.LT_AUDIT_TRIAL_EVENTS, request.getData(),
					null, null, null, null);
			messageEvent.setKey(1);
			messageEvent.setTopic(KafkaTopics.LT_AUDIT_TRIAL_EVENTS);
			messageEvent.setRequest(request.getData());

		} catch (Exception e) {
			throw new FailedException("Failed to save message event!!");
		}
		try {
			this.producerService.publishToKafka(1, KafkaTopics.LT_AUDIT_TRIAL_EVENTS,
					AppUtil.convertJsonToString(request), messageEvent);
		} catch (Exception e) {
//			throw new FailedException("Failed to produce kafka to LT_AUDIT_TRIAL_EVENTS !");
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE, e.getMessage());
			this.messageEventService.saveEvent(testEvent);
		}
	}

}
