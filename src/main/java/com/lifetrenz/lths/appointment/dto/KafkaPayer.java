package com.lifetrenz.lths.appointment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KafkaPayer {
	private String system;
	private String code;
	private String display;
	private String payerId;
	private String encounterId;
	private CommonDetails companyTypeMaster;
	private CommonDetails companyMaster;
	private CommonDetails tariffMaster;
	private CommonDetails financialClassMaster;
	private CommonDetails contractMaster;
	private CommonDetails associatedCompanyMaster;
	private CommonDetails glTypeMaster;
	private Long startDate;
	private Long endDate;
	private String priority;
	private Boolean status;
	private String unitCode;
	private String orgCode;
	private String createdBy;
	private String updatedBy;
	private String policyNo;
}
