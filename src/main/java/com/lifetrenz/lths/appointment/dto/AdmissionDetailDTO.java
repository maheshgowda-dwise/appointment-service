package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.enums.AdmissionSessionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdmissionDetailDTO {

	private String id;

	private ClinicalSystemMasterDTO admitStatus;

	private ClinicalSystemMasterDTO journeyStatus;

	private String admitType;

	private Date admittedOn;

	private String admittedBy;

	private UserDetails admittingDoctor;

	private PatLocationDTO admittingLocation;

	private PatLocationDTO currentLocation;

	private ClinicalSystemMasterDTO specialization;

	// private List<AdministrativeEventDetails> administrativeEvent;

	private Date arrivalTime;

	private String endDate;

	private Date startConsultDate;

	private String startDate;

	private String dischargedOn;

	private String dischargeType;

	public String startNursingDate;

	public String signedBy;

	// private List<CareTeam> careTeam;

	private String externalVisitId;

	private Long defaultPriceBookId;

	private AdmissionSessionStatus admissonNoteSignStatus;
	
	private ClinicalSystemMasterDTO admitPurpose;

    private String department;
	
	private String bedCategory;
	
    private Long bedCategoryId;
    
    private String admissionCategoryIdentifierCode;

}
