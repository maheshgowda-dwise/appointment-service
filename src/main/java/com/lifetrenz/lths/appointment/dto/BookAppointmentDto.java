package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import com.lifetrenz.lths.appointment.model.value_object.AppointmentSite;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;
import com.lifetrenz.lths.appointment.model.value_object.ParticipantDetails;
import com.lifetrenz.lths.appointment.model.value_object.Remarks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAppointmentDto implements Serializable {

	private static final long serialVersionUID = 1187105802585151504L;

	private String id;

	@NotNull(message = "Participants not found!!")
	private List<ParticipantDetails> particpantCalendar;

	private SystemMasterDTO appointmentCategory;

	private SystemMasterDTO appointmentType;

	private SystemMasterDTO appointmentStatus;

	private SystemMasterDTO payerType;

	private SystemMasterDTO visitType;

	private SystemMasterDTO appointmentPriority;

	@NotNull(message = "Conduct mode is mandatory!!")
	private SystemMasterDTO appointmentConductMode;

	@NotNull(message = "Booking source is mandatory!!")
	private SystemMasterDTO appointmentBookingSource;

	private SystemMasterDTO appointmentReferralCategory;

	@NotNull(message = "Booking mode is mandatory!!")
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

	private String clinicalScheduleId;

	private CustomerTransactionBase transactionBase;

	private String slotId;

	private String teleconsultChannelId;

	private SystemMasterDTO encounterStatus;

	private SystemMasterDTO admitStatus;

	private SystemMasterDTO journeyStatus;

	private Boolean isReschedule;

	private Boolean isVip;

	private VisitDetails visitDetails;

}
