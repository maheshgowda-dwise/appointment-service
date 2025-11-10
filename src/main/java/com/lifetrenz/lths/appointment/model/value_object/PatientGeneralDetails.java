/**
 * 
 */
package com.lifetrenz.lths.appointment.model.value_object;

import java.util.Date;

import com.lifetrenz.lths.appointment.dto.NewTelecomDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientGeneralDetails {
	
	private Long id;

	private String mpi;

	private String externalMpi;
	
	private String fullName;

	private String profilePhotoUrl;

	private String gender;

	private Date dob;

	private String age;
	
	private String startDate;

	private NewTelecomDTO telecom;

	private String emailId;
}
