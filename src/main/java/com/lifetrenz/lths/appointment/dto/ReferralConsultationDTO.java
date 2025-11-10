package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.enums.ReferralType;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;
import com.lifetrenz.lths.appointment.model.value_object.SystemMaster;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferralConsultationDTO {

	private PatientDetailDTO patient;

	private CustomerTransactionBase transAttribute;

	private Long admissionSiteId;

	private String identifier;

	private ReferralType referralTypes;

	private Integer referralType;

	private Long referralSiteId;

	private String referralSiteName;

	private SystemMaster specialisation;

	private String physicianName;

	private String physicianId;
	
	private String specialisationIdentifier;

	private SystemMaster priority;

	private Date preferedDate;

	private String referralNotes;

	private SystemMaster referralReason;

	private String referralId;

	private String encounterId;
	
	private String appointmentId;
	
	private Long genderId;

	private String gender;

	private String dateOfBirth;
	
	private String referredDocname;
	
	private Long referredDocId;
	
	private String referredDocSpecialisation;
	
	private String referredSpecialisationIdentifier;
	
	private String feedBack;
	
	private Long referredSiteId;
	
	private String referredSiteName;
}
