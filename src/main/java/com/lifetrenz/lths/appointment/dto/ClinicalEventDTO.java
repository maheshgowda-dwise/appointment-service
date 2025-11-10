package com.lifetrenz.lths.appointment.dto;

import com.lifetrenz.lths.appointment.enums.ClinicalEventType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
*
* @author Sai.KVSS
*
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClinicalEventDTO {
	private ClinicalEventType eventType;

	private String data;
}
