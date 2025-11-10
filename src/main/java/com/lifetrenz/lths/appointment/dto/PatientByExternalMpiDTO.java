/**
 * 
 */
package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

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
public class PatientByExternalMpiDTO {

	private Long patientId;

	private String mpi;

	private String externalMpi;

	private String name;

	private String gender;

	private Date dob;

	private TelecomDTO telecom;

}
