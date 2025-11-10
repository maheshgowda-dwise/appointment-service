package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelayBookAppointmentDto implements Serializable {

	private static final long serialVersionUID = 9169423459442729272L;

	private String mrn;

	private RelayPatientRegistrationDTO kafkaEMRPatient;

	private LTABookAppointment bookAppointmentDTO;

	private CustomerTransactionAttributeDTO customerTransactionAttributeDTO;
	private CommonDetails departmentMaster;

	// *********** this DTO used as kafka make sure handled properly ******* //

}
