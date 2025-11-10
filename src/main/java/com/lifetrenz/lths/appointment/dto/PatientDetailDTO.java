package com.lifetrenz.lths.appointment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientDetailDTO {

	private Long patientId;

	private String patientName;

	private ClinicalSystemMasterDTO gender;

	private String age;

	private String bloodGroup;

	private String dob;

	private String mpi;

	private String externalMPI;

	private TelecomDTO telecomNumber;

	private String profilePhotoUrl;

	private String nationality;

	private Boolean isMaternity;

	private Boolean isVip;

	private String identificationType;

}
