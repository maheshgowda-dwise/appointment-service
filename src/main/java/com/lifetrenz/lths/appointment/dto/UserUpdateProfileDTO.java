package com.lifetrenz.lths.appointment.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.model.value_object.Address;
import com.lifetrenz.lths.appointment.model.value_object.Language;
import com.lifetrenz.lths.appointment.model.value_object.Name;
import com.lifetrenz.lths.appointment.model.value_object.SystemMaster;
import com.lifetrenz.lths.appointment.model.value_object.UserSite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUpdateProfileDTO {

	private String id;
	private Long coreUserId;
	private Name name;
	private Language appLanguage;
	private Long employeId;
	private List<Language> knownLanguage;
	private SystemMaster gender;
	private TelecomDTO telecom;
	private List<QualificationDTO> userQualifications;
	private List<WorkExperienceDTO> workExperiences;
	private String professionalStatement;
	private String preference;
	private String email;
	private Address address;
	private String medicalCouniclNo;
	private List<SystemMaster> specialties;
	private Long specializationId;
	private List<UserSite> sites;
	private KafkaTransactionBase transactionBase;
	private Boolean isLocalTime;
	private String profilePhoto;
	private Boolean portalEnable;
	private Boolean isScheduled;
	private String designation;
	private Long countryId;
	private String salutationName;
	private List<Department> department;

}
