package com.lifetrenz.lths.appointment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenPayLoad {

	private String name;

	private String preferred_username;

	private String given_name;

	private String family_name;

	private String email;

	private Long customerBusinessId;

	private Long customerId;

	private Long coreUserId;

	private Long departmentId;

	private String department;

}
