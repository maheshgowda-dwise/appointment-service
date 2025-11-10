package com.lifetrenz.lths.appointment.model.value_object;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSite implements Serializable{

	private static final long serialVersionUID = 3595798941636171738L;
	
	private Long id;
	
	private String siteName;

}
