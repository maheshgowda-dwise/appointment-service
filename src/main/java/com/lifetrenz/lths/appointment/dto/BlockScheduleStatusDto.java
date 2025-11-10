package com.lifetrenz.lths.appointment.dto;

import com.lifetrenz.lths.appointment.model.enums.BlockStatus;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockScheduleStatusDto {

	private String id;

	private String eventIdentifier;

	private BlockStatus status;

	private CustomerTransactionBase customerTrasaction;
}
