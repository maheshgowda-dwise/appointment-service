package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

import com.lifetrenz.lths.appointment.common.enums.SlotReservation;
import com.lifetrenz.lths.appointment.common.enums.SlotStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Slot implements Serializable {

	private static final long serialVersionUID = 6241537828503578877L;

	private String id;

	private LocalTime startTime;

	private LocalTime endTime;

	private SlotStatus status;

	private SlotReservation reservation;

	private int allowedWaitList;

	private int consumedWaitList;
	
	private Date startDate;

	private Date endDate;
}
