package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.model.enums.BlockStatus;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockSchedularDto {

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
