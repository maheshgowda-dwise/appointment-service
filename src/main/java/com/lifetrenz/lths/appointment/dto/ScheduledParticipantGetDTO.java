package com.lifetrenz.lths.appointment.dto;

import com.lifetrenz.lths.appointment.model.collection.User;
import com.lifetrenz.lths.appointment.model.value_object.Ambulance;
import com.lifetrenz.lths.appointment.model.value_object.Equipment;
import com.lifetrenz.lths.appointment.model.value_object.Location;
import com.lifetrenz.lths.appointment.model.value_object.Role;
import com.lifetrenz.lths.appointment.model.value_object.SystemMasterNew;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledParticipantGetDTO {

	private String id;
	
	private Long participantId;
	
	private Long conductingSiteId;
	
	private SystemMasterNew participantType;
	
	private SystemMasterNew calendarType;
	
	private User participantUser;
	
	private ScheduleParticipantUser scheduleParticipantUser;
	
	private Equipment equipment;

	private Ambulance ambulance;

	private Location location;

	private Role role;
	
	private Long scheduleCount;
	
	private Boolean isLogin;
	
	private String consultingLocation;
	
	private Long customerId;
	
	private Long customerBusinessId;
	
	private Long siteId;
}
