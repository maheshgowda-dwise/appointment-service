package com.lifetrenz.lths.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LTCSPendingRecancellationAppointment {

	private Long appointmentid;

	private String appointmentStauts;


}
