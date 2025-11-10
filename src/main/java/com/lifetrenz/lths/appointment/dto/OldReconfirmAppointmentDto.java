package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.model.collection.Slots;
import com.lifetrenz.lths.appointment.model.value_object.AppointmentSite;
import com.lifetrenz.lths.appointment.model.value_object.ClinicalActivityDetails;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;
import com.lifetrenz.lths.appointment.model.value_object.OrderItems;
import com.lifetrenz.lths.appointment.model.value_object.OrderingPhysician;
import com.lifetrenz.lths.appointment.model.value_object.ParticpantCalendar;
import com.lifetrenz.lths.appointment.model.value_object.Remarks;
import com.lifetrenz.lths.appointment.model.value_object.SystemMaster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OldReconfirmAppointmentDto extends CustomerTransactionBase implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String doctorId;

	private String doctorName;

	private Long coreAppointmentId;

	private List<SystemMaster> specialties;

	private String patientId;

	private String patientName;

	private String refDoctorId;

	private String refDoctorName;

	private String transferSourseId;

	private OrderingPhysician orderingPhysician;

	private List<ParticpantCalendar> particpantCalendar;

	private String appointmentCategory;

	private String appointmentType;

	private String appointmentStatus;

	private List<Slots> slots;

	private List<OrderItems> orderItems;

	private SystemMasterDTO payerType;

	private SystemMasterDTO visitType;

	private String appointmentPriority;

	private SystemMasterDTO appointmentConductMode;

	private String appointmentBookingSource;

	private String appointmentReferralCategory;

	private String appointmentBookingMode;

	private Long appointmentStartDate;

	private Long appointmentEndDate;

	private String durationinMinutes;

	private String instructions;

	private String administrativeNotes;

	private String reconfirmedOn;

	private String reconfirmedReason;

	private String cancelledReason;

	private Boolean isWaitingList;

	private String externalAppointmentId;

	private Remarks remarks;

	private Boolean isNewVisit;

	private String username;

	private ClinicalActivityDetails clinicalactivitydetails;
	
	private String appointmentServiceType;
	
	private String appointmentReferenceId;
		
	private String mpi;
	
	private Date dob;

	private String gender;
	
	private String patientPhone;

	private String email;
	
	private AppointmentSite appointmentSite;
	
	private String teleconsultChannelId;
	
	private String slotId;
	
	private String mobilityAppointmentId;
}
