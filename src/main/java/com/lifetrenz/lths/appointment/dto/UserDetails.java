package com.lifetrenz.lths.appointment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetails {
	private String id;

	private Long coreUserId;

	private String fullName;
	
	private DoctorRoleDetails role;

	private String emailId;

	private TelecomDTO telecom;
}
