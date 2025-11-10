package com.lifetrenz.lths.appointment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 
 * @author Ayush.P
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitDetails {
	
	private CustomerMasterDTO patientCategorys;
	private CustomerMasterDTO patientConditions;
	private CustomerMasterDTO patientCriticalitys;
	private CustomerMasterDTO priorirtys;
	private CustomerMasterDTO vipCategorys;

	
	

}
