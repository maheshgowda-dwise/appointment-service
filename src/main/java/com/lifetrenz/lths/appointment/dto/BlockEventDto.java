package com.lifetrenz.lths.appointment.dto;

import com.lifetrenz.lths.appointment.model.enums.BlockStatus;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockEventDto {

	private String slotId;

	private Long startDate;

	private Long endDate;

	private String patientName;

	private Long patientId;

	private String userName;

	private Long userId;

	private String schedularEventId;

	private String eventIdentifier;

	private BlockStatus status;

	private CustomerTransactionBase customerTrasaction;

}
