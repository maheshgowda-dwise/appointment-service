package com.lifetrenz.lths.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncounterDetailDTO {

	private String encounterId;

	private String displayEncounterId;

	private ClinicalSystemMasterDTO encounterStatus;

	private String type;

	private ClinicalSystemMasterDTO payerType;

	private ClinicalSystemMasterDTO paymentStatus;

}
