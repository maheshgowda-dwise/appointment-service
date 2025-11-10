package com.lifetrenz.lths.appointment.dto;

import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedularEventBlockDto {

	private Long id;

	private CustomerTransactionBase customerTrasaction;

}
