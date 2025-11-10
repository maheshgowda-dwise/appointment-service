package com.lifetrenz.lths.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Sai.KVS
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorVO {
	private String id;
	private String username;
	private String name;
	private String doctorCode;
}
