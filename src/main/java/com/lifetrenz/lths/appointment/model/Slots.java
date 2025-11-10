package com.lifetrenz.lths.appointment.model;

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
public class Slots {

	private String slotId;

	private String slotStatus;

	private String intervel;

	private String avilablefromTime;

	private String avilabletoTime;
	
}
