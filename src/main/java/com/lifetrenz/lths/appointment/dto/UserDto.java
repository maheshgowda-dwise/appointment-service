package com.lifetrenz.lths.appointment.dto;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.model.value_object.Address;
import com.lifetrenz.lths.appointment.model.value_object.Language;
import com.lifetrenz.lths.appointment.model.value_object.Location;
import com.lifetrenz.lths.appointment.model.value_object.Name;
import com.lifetrenz.lths.appointment.model.value_object.SystemMaster;
import com.lifetrenz.lths.appointment.model.value_object.UserSite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Mujaheed.N
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

	private String doctorId;

	private String doctorName;

	private List<SystemMaster> specialties;

	private String nationality;

	private String userQualification;

	private SystemMaster gender;

	private String fromDate;

	private String toDate;

	private Language language;
	

	@Id
	private String id;

	private Language appLanguage;

	private List<Language> knownLanguage;

	private Long coreUserId;

	private Name name;

	private TelecomDTO telecom;

	private String email;

	private WorkExperienceDTO workExperience;

	private String professionalStatement;

	private String preference;

	private Address address;

	private String username;

	private String password;

	private Long specializationId;

	private String medicalCouniclNo;

	private List<UserSite> sites;

	private KafkaTransactionBase kafkatransactionBase;

	private Location location;

	
}
