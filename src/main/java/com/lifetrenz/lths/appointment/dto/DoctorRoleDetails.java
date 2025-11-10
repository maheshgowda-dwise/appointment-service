package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRoleDetails implements Serializable{

	private static final long serialVersionUID = 7995944134933689481L;
	
	private String id;
	
	private String name;
	
	private String identifier;

}
