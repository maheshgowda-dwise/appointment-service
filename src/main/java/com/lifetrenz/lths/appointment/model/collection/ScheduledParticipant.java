/**
 * 
 */
package com.lifetrenz.lths.appointment.model.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.lifetrenz.lths.appointment.dto.ScheduleParticipantUser;
//gitlab.lifetrenz.com/Dilshad.A/appointment-service.git
import com.lifetrenz.lths.appointment.model.value_object.Ambulance;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;
import com.lifetrenz.lths.appointment.model.value_object.Equipment;
import com.lifetrenz.lths.appointment.model.value_object.Location;
import com.lifetrenz.lths.appointment.model.value_object.Role;
import com.lifetrenz.lths.appointment.model.value_object.Speciality;
import com.lifetrenz.lths.appointment.model.value_object.SystemMasterNew;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Ajith.K
 *
 */
@Document(collection = "scheduled_participant")
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledParticipant extends CustomerTransactionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	private Long participantId;
	
	private Long conductingSiteId;
	
	private SystemMasterNew participantType;
	
	private SystemMasterNew calendarType;
	
	private User participantUser;
	
	private ScheduleParticipantUser scheduleParticipantUser;
	
	private Equipment equipment;
	
	private Speciality speciality;

	private Ambulance ambulance;

	private Location location;

	private Role role;
	
	private Long scheduleCount;
	
	private Boolean isLogin;
	
	private String consultingLocation;
}
