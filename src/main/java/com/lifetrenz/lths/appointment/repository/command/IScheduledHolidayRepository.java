package com.lifetrenz.lths.appointment.repository.command;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lifetrenz.lths.appointment.model.collection.ScheduleHoliday;

public interface IScheduledHolidayRepository extends MongoRepository<ScheduleHoliday, String> {

	ScheduleHoliday findByHolidayNameContainingIgnoreCase(String holidayName);

	List<ScheduleHoliday> findByHolidaySiteIdAndHolidayDate(Long holidaySiteId, Date holidayDate);

}
