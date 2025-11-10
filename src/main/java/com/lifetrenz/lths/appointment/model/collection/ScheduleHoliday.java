package com.lifetrenz.lths.appointment.model.collection;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "site_holiday")
@Data

@NoArgsConstructor
@AllArgsConstructor
public class ScheduleHoliday {
	@Id
	private String id;

	private String description;

	private String holidayName;

	private Date holidayDate;

	private Long holidaySiteId;

	private CustomerTransactionBase customerTrasaction;

}
