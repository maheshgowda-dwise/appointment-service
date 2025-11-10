/**
 * 
 */
package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

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
public class DoctorSlots implements Serializable {

	private static final long serialVersionUID = 5848104876033550185L;
	
	private String doctorId;
	
	private String doctorName;
	
	private String participantType;

	private LocalDate date;

	private String siteId;

	private int totalWaitList;

	private int consumedWaitList;

	private Collection<Slot> slots;
}
