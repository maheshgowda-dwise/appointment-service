package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

import com.lifetrenz.lths.appointment.common.enums.ClinicalScheduleStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Shanmukha.G
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClinicalScheduleDTO {

	private Date scheduleDate;

	private Long scheduleId;

	private Long encounterId;

	private Long admissionId;

	private Long appointmentId;

	private String status;

	private Long userId;

	private String condustStatusIdentifier;

	private ClinicalScheduleStatus scheduleStatus;

}
