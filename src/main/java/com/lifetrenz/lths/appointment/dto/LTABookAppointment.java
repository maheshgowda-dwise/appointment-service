package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.model.value_object.ClinicalActivityDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LTABookAppointment implements Serializable {

	private static final long serialVersionUID = 2769894183094800113L;

	private Long id;

	private CustomerTransactionAttributeDTO siteTransactionAttribute;

	private Long personId;

	private List<ParticpantCalendarDTO> participant;

	private SystemMasterDTO serviceCategoryIdentifier;

	private SystemMasterDTO serviceTypeIdentifier;

	private SystemMasterDTO statusIdentifier;

	private SystemMasterDTO payerTypeIdentifier;

	private SystemMasterDTO visitTypeIdentifier;

	private SystemMasterDTO priorityIdentifier;

	private SystemMasterDTO conductModeIdentifier;

	private SystemMasterDTO bookingSourceIdentifier;

	private SystemMasterDTO referralCategoryIdentifier;

	private SystemMasterDTO bookingModeIdentifier;

	private SystemMasterDTO participantTypeIdentifier;

	private String externalAppointmentId;

	private Long startDate;

	private Long endDate;

	private String durationinMinutes;

	private String instructions;

	private String administrativeNotes;

	private String visitNoteTypeIdentifier;

	private Boolean iswaitingList;

	private Boolean isVisitToDepartment;

	private Long scheduleId;

	private String username;

	private String slotId;

	private ClinicalActivityDetails clinicalActivityDetails;

	private Boolean isEncounter; // used to check encounter appointment or not

	private String referenceAppointmentId;

	private String externalVisitId;

	private String displayEncounterNo;

	private String tokenNumber;

	private InsuranceDetails insuranceDetails;

	private String mobilityAppointmentId;
	


	// *********** this DTO used as kafka make sure handled properly ******* //

}
