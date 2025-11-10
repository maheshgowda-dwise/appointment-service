/**
 * 
 */
package com.lifetrenz.lths.appointment.dto;

import java.util.List;

import com.lifetrenz.lths.appointment.model.value_object.Address;
import com.lifetrenz.lths.appointment.model.value_object.Language;
import com.lifetrenz.lths.appointment.model.value_object.Name;
import com.lifetrenz.lths.appointment.model.value_object.SystemMaster;
import com.lifetrenz.lths.appointment.model.value_object.UserSite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ajith.K
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDetailListDto {

	private String id;

	private Language appLanguage;

	private List<Language> knownLanguage;

	private Long coreUserId;

	private Name name;

	private TelecomDTO telecom;

	private String email;

	private List<QualificationDTO> userQualification;

	private List<WorkExperienceDTO> workExperience;

	private String professionalStatement;

	private String preference;

	private SystemMaster gender;

	private String[] nationality;

	private Address address;

	private String username;

	private String password;

	private List<SystemMaster> specialties;

	private Long specializationId;

	private String medicalCouniclNo;

	private List<UserSite> sites;

	private TransactionBase transactionBase;

	private Location location;

	private String profilePhoto;

	private String signatureUrl;

	private Boolean portalEnable;

	private Boolean isScheduled;

	private String designation;

	private String totalSlots;

	private String bookedSlot;
}
