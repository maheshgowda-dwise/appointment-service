package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelayPatientRegistrationDTO implements Serializable {

	private static final long serialVersionUID = 3477843311102231393L;
	private Long personId;
	private String mrn;
	private String trn;
	private KafkaName patientName;
	private String patientId;
	private String identificationTypeMaster;
	private String identificationType;
	private String identificationNo;
	private String prefixMaster;
	private String gender;
	private String dob;
	private Boolean deceased;
	private String patientCategoryMaster;
	private Boolean isCtos;
	private Boolean isBlocked;
	private String primaryAddress;
	private String email;
	private String countryMaster;
	private String stateMaster;
	private String cityMaster;
	private String areaMaster;
	private String pinCode;
	private String countryCode;
	private String mobileNo;
	private String patientNotes;
	private Long registrationDate;
	private Boolean active = false;
	private String unitCode;
	private String orgCode;
	private String createdBy;
	private String updatedBy;
	private String type;
	private String bloodType;
	private CustomerTransactionAttributeDTO customerTransactionAttributeDTO;
	private String imageUrl;
	private String image;


}
