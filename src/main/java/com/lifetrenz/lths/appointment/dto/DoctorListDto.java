/**
 * 
 */
package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

import com.lifetrenz.lths.appointment.model.value_object.SystemMaster;

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
public class DoctorListDto {

	private Long coreUserId;

	private String fullName;

	private SystemMaster gender;

	private SystemMaster specialtiy;

	private String profilePhoto;
	
	private boolean isDoctorAvailable;
	
	private Date nextAvailable;
	
	private int totalSlots;

}
