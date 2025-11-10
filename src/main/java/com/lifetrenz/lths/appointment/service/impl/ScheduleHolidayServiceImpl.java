package com.lifetrenz.lths.appointment.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.common.enums.KafkaTopic;
import com.lifetrenz.lths.appointment.common.util.AppUtils;
import com.lifetrenz.lths.appointment.common.util.KafkaTopics;
import com.lifetrenz.lths.appointment.dto.ScheduleHolidayDTO;
import com.lifetrenz.lths.appointment.dto.ScheduleHolidayEventDTO;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.enums.ScheduleHolidayType;
import com.lifetrenz.lths.appointment.mapper.EventsMapper;
import com.lifetrenz.lths.appointment.mapper.ScheduleHolidayMapper;
import com.lifetrenz.lths.appointment.model.collection.ScheduleHoliday;
import com.lifetrenz.lths.appointment.model.events.MessageEvent;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;
import com.lifetrenz.lths.appointment.repository.command.IScheduledHolidayRepository;
import com.lifetrenz.lths.appointment.service.ProducerService;
import com.lifetrenz.lths.appointment.service.ScheduleHolidayService;
import com.lifetrenz.lths.appointment.util.AppUtil;

@Component
public class ScheduleHolidayServiceImpl implements ScheduleHolidayService {

	@Autowired
	IScheduledHolidayRepository iScheduledHolidayRepository;

	@Autowired
	ScheduleHolidayMapper mapper;

	@Autowired
	EventsMapper eventsMapper;

	@Autowired
	ProducerService producerService;

	@Override
	public ScheduleHolidayDTO saveHoildayDate(ScheduleHolidayDTO scheduleHolidayDTO, TokenPayLoad tokenPayLoad,
			String path) throws Exception {
		ScheduleHolidayDTO result = null;

		try {
			List<ScheduleHoliday> existingHolidays = iScheduledHolidayRepository.findByHolidaySiteIdAndHolidayDate(
					scheduleHolidayDTO.getHolidaySiteId(), scheduleHolidayDTO.getHolidayDate());

			if (existingHolidays != null && !existingHolidays.isEmpty()) {
				throw new Exception("A holiday already exists for this site on " + scheduleHolidayDTO.getHolidayDate());
			}

			ScheduleHoliday entity = mapper.mapDtoToEntity(scheduleHolidayDTO, tokenPayLoad);

			ScheduleHoliday savedEntity = iScheduledHolidayRepository.save(entity);

			result = mapper.mapEntityToDto(savedEntity);

			if (savedEntity.getId() != null) {
				result.setTokenPayLoad(tokenPayLoad);
				result.setSiteId(scheduleHolidayDTO.getCustomerTrasaction().getSiteId());

				ScheduleHolidayEventDTO kafkaScheduleHolidayDTO = new ScheduleHolidayEventDTO(
						ScheduleHolidayType.ADD_SCHEDULE_HOLIDAY, AppUtil.convertJsonToString(result));

				MessageEvent messageEvent = this.eventsMapper.convertToMessageEvent(
						AppUtil.convertJsonToString(kafkaScheduleHolidayDTO), KafkaTopic.LT_SCHEDULE_EVENT.value,
						KafkaTopics.LT_SCHEDULE_EVENT.toString(), null, tokenPayLoad.getPreferred_username());

				this.producerService.publishToKafka(Integer.valueOf(KafkaTopic.LT_SCHEDULE_EVENT.value),
						KafkaTopics.LT_SCHEDULE_EVENT, AppUtil.convertJsonToString(kafkaScheduleHolidayDTO),
						messageEvent);
			}

			return result;

		} catch (Exception e) {
			throw new Exception("Failed To Save");
		}
	}

	@SuppressWarnings("unused")
	private void publishKafkaSchedule(ScheduleHolidayDTO scheduleHoliday, TokenPayLoad tokenPayLoad, String path)
			throws Exception {

		MessageEvent messageEvent = this.eventsMapper.convertToMessageEvent(
				AppUtil.convertJsonToString(scheduleHoliday), KafkaTopic.LT_SCHEDULE_EVENT.value,
				KafkaTopics.LT_SCHEDULE_EVENT.toString(), null, tokenPayLoad.getPreferred_username());

		this.producerService.publishToKafka(Integer.valueOf(KafkaTopic.LT_SCHEDULE_EVENT.value),
				KafkaTopics.LT_SCHEDULE_EVENT.toString(), AppUtils.convertJsonToString(scheduleHoliday), messageEvent);

	}

	@Override
	public Boolean deleteSchedulHoliday(TokenPayLoad tokenPayLoad, String id) throws ApplicationException {

		ScheduleHoliday result = iScheduledHolidayRepository.findById(id).get();
		if (result != null) {

			this.iScheduledHolidayRepository.deleteById(id);
			ScheduleHolidayEventDTO kafkaScheduleHolidayDTO = new ScheduleHolidayEventDTO(
					ScheduleHolidayType.DELETE_SCHEDULE_HOLIDAY, AppUtil.convertJsonToString(result));

			MessageEvent messageEvent = this.eventsMapper.convertToMessageEvent(
					AppUtil.convertJsonToString(kafkaScheduleHolidayDTO), KafkaTopic.LT_SCHEDULE_EVENT.value,
					KafkaTopics.LT_SCHEDULE_EVENT.toString(), null, tokenPayLoad.getPreferred_username());

			try {
				this.producerService.publishToKafka(Integer.valueOf(KafkaTopic.LT_SCHEDULE_EVENT.value),
						KafkaTopics.LT_SCHEDULE_EVENT, AppUtil.convertJsonToString(kafkaScheduleHolidayDTO),
						messageEvent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;

		}

		return false;
	}

	@Override
	public List<ScheduleHolidayDTO> getAllScheduleHoilday() throws ApplicationException {

		return StreamSupport.stream(iScheduledHolidayRepository.findAll().spliterator(), false).map(t -> {
			try {
				return mapper.mapEntityToDto(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());

	}

	public ScheduleHolidayDTO getScheduleHoilday(String holidayName) throws ApplicationException {
		ScheduleHoliday entity = iScheduledHolidayRepository.findByHolidayNameContainingIgnoreCase(holidayName);

		try {
			return mapper.mapEntityToDto(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Boolean updateSchedulHoliday(TokenPayLoad tokenPayLoad, ScheduleHolidayDTO scheduleHolidayDTO)
			throws Exception {

		Optional<ScheduleHoliday> optionalHoliday = iScheduledHolidayRepository.findById(scheduleHolidayDTO.getId());
		if (!optionalHoliday.isPresent()) {
			throw new IllegalArgumentException("ScheduleHoliday not found with id: " + scheduleHolidayDTO.getId());
		}

		ScheduleHoliday existingHoliday = optionalHoliday.get();

		List<ScheduleHoliday> sameDayHolidays = iScheduledHolidayRepository.findByHolidaySiteIdAndHolidayDate(
				scheduleHolidayDTO.getHolidaySiteId(), scheduleHolidayDTO.getHolidayDate());

		boolean duplicateExists = sameDayHolidays.stream().anyMatch(h -> !h.getId().equals(existingHoliday.getId()));

		if (duplicateExists) {
			throw new Exception("A holiday already exists for this site on " + scheduleHolidayDTO.getHolidayDate());
		}

		CustomerTransactionBase cts = new CustomerTransactionBase();
		cts.setCustomerId(scheduleHolidayDTO.getCustomerTrasaction().getCustomerId());
		cts.setCustomerBusinessId(scheduleHolidayDTO.getCustomerTrasaction().getCustomerBusinessId());

		existingHoliday.setDescription(scheduleHolidayDTO.getDescription());
		existingHoliday.setHolidayName(scheduleHolidayDTO.getHolidayName());
		existingHoliday.setHolidayDate(scheduleHolidayDTO.getHolidayDate());
		existingHoliday.setCustomerTrasaction(cts);

		iScheduledHolidayRepository.save(existingHoliday);

		try {
			ScheduleHolidayEventDTO kafkaScheduleHolidayDTO = new ScheduleHolidayEventDTO(
					ScheduleHolidayType.UPDATE_SCHEDULE_HOLIDAY, AppUtil.convertJsonToString(existingHoliday));

			MessageEvent messageEvent = this.eventsMapper.convertToMessageEvent(
					AppUtil.convertJsonToString(kafkaScheduleHolidayDTO), KafkaTopic.LT_SCHEDULE_EVENT.value,
					KafkaTopics.LT_SCHEDULE_EVENT.toString(), null, tokenPayLoad.getPreferred_username());

			this.producerService.publishToKafka(Integer.valueOf(KafkaTopic.LT_SCHEDULE_EVENT.value),
					KafkaTopics.LT_SCHEDULE_EVENT, AppUtil.convertJsonToString(kafkaScheduleHolidayDTO), messageEvent);

		} catch (Exception e) {
			throw new RuntimeException("Failed to publish update event to Kafka", e);
		}

		return true;
	}

}
