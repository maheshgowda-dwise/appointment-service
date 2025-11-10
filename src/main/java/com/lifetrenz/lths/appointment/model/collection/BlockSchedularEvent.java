package com.lifetrenz.lths.appointment.model.collection;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.model.enums.BlockStatus;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "block_scheduler_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockSchedularEvent {

	@Id
	private String id;

	private String userName;

	private Long userId;

	private String patientName;

	private Long patientId;

	private String schedularEventId;

	private String eventIdentifier;

	private Date blockedDate;

	private BlockStatus status;

	private CustomerTransactionBase customerTrasaction;

}
