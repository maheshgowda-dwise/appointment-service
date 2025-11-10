package com.lifetrenz.lths.appointment.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class ScheduleParticipantUser {
	
	private String id;

	private Long coreUserId;

	private Name name;

	private TelecomDTO telecom;

	private String email;

	private SystemMaster gender;

	private List<SystemMaster> specialties;

	private List<UserSite> sites;


}
