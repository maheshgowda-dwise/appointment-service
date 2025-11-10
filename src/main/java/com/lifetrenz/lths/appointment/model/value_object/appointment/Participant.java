/**
 * 
 */
package com.lifetrenz.lths.appointment.model.value_object.appointment;

import com.lifetrenz.lths.appointment.model.value_object.SystemMasterNew;

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
public class Participant {

	private String participantId;

	private String participantName;

	private SystemMasterNew appointmentParticipantType;
}
