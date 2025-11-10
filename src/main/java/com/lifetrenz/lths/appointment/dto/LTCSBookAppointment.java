/**
 * 
 */
package com.lifetrenz.lths.appointment.dto;

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
public class LTCSBookAppointment {

	private Long id;

	private CSSiteTransactionAttributeDTO siteTransactionAttribute;

	private Long personId;

	private List<ParticpantCalendarDTO> participant;

	private String serviceCategoryIdentifier;

	private String serviceTypeIdentifier;

	private String statusIdentifier;

	private String payerTypeIdentifier;

	private String visitTypeIdentifier;

	private String priorityIdentifier;

	private String conductModeIdentifier;

	private String bookingSourceIdentifier;

	private String referralCategoryIdentifier;

	private String bookingModeIdentifier;

	private String participantTypeIdentifier;

	private String externalAppointmentId;

	private Long startDate;

	private Long endDate;

	private String durationinMinutes;

	private String instructions;

	private String administrativeNotes;

	private String visitNoteTypeIdentifier;

	private Boolean iswaitingList;

	private Boolean isVisitToDepartment;

	private ClinicalActivityDetails clinicalActivityDetails;

	private Long scheduleId;

	private String appointmentServiceType;

	private String username;

	private String referenceAppointmentId;

	private Boolean isEncounter; // used to check encounter appointment or not

	private String externalVisitId;

	private String displayEncounterNo;

	private String tokenNumber;
	private String identificationType;

	private InsuranceDetails insuranceDetails;

	private String mobilityAppointmentId;

	private Boolean isMarkArrive;

	private Boolean isReschedule;

	private String rescheduledId;

	private String preferred_username;

	private Long customerId;

	private Long customerBussinessId;

	private Long coreUserId;

	private List<ServiceOrderItemDto> serviceItem;
	private Boolean isVip;

	private VisitDetails visitDetails;

	private Boolean isEmergency;

	private Boolean isTemporaryPatient;
	
// ---------ONLY FOR HEALTH SCREEN PACKAGE---------------------------------
	private Boolean isHealthScreenPackage;

	private Long admissionId;

	private Long appointmentId;

	private Long encounterId;
	
//------------------------------------------------------------------------
	private String packageApplicableType;
	
	private String orderDetails;
	
	private Long patientPackageId;
	
	private Long patientPackageItemId;

	// *********** this DTO used as kafka make sure handled properly ******* //
	
//-----------------------------------------
	private Long refAdmissionId;
	
	private Long doctorDepartmentId;

	private Boolean isReferralAppointment;
	
	private String referralId;

}
