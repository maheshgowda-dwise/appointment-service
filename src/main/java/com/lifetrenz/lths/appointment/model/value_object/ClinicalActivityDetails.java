package com.lifetrenz.lths.appointment.model.value_object;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClinicalActivityDetails {

	private List<ClinicalItem> clinicalItem;

	private Long orderId;

	private Long scheduledId;

	private String scheduleStatus;

	public String tumorBoardId;
	
	private String tumborBoardScheduleId;
	
	private String clinicalScheduleId;
	
	private String clinicalOrderId;

}
