package com.lifetrenz.lths.appointment.model.collection;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.lifetrenz.lths.appointment.dto.SystemMasterDTO;
import com.lifetrenz.lths.appointment.dto.VisitDetails;
import com.lifetrenz.lths.appointment.model.value_object.AppointmentSite;
import com.lifetrenz.lths.appointment.model.value_object.ClinicalActivityDetails;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;
import com.lifetrenz.lths.appointment.model.value_object.ParticipantDetails;
import com.lifetrenz.lths.appointment.model.value_object.Remarks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "appointments")
@AllArgsConstructor
@NoArgsConstructor
public class Appointment implements Serializable {

	private static final long serialVersionUID = 576365136629263503L;

	@Id
	private String id;

	private List<ParticipantDetails> particpantCalendar;

	private SystemMasterDTO appointmentCategory;

	private SystemMasterDTO appointmentType;

	private SystemMasterDTO appointmentStatus;

	private SystemMasterDTO payerType;

	private SystemMasterDTO visitType;

	private SystemMasterDTO appointmentPriority;

	private SystemMasterDTO appointmentConductMode;

	private SystemMasterDTO appointmentBookingSource;

	private SystemMasterDTO appointmentReferralCategory;

	private SystemMasterDTO appointmentBookingMode;

	private Date startDate;

	private Date endDate;

	private String durationinMinutes;

	private String instructions;

	private String administrativeNotes;

	private Date reconfirmedOn;

	private String reconfirmedReason;

	private Boolean isWaitingList;

	private String externalAppointmentId;

	private String cancelledReason;

	private List<Remarks> remarks;

	private Boolean isNewVisit;

	private AppointmentSite appointmentSite;

	private ClinicalActivityDetails clinicalactivitydetails;

	private String clinicalScheduleId;

	private CustomerTransactionBase transactionBase;

	private String slotId;

	private Long startDateEpoc;

	private Long endDateEpoc;

	private String refDoctorId;

	private String refDoctorName;

	private String transferSourseId;

	private String teleconsultChannelId;

	private SystemMasterDTO encounterStatus;

	private SystemMasterDTO admitStatus;

	private SystemMasterDTO journeyStatus;

	private Boolean isReschedule;
	
	private Boolean isEmergency;
	
	private Boolean isVip;

	private VisitDetails visitDetails;
	
	private Boolean isReferralAppointment;

}
