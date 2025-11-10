package com.lifetrenz.lths.appointment.dto;

import com.lifetrenz.lths.appointment.model.enums.RelayEventType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Dilshad.A
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelayEventDTO {
	private RelayEventType eventType;

	private String data;
}
