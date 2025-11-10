package com.lifetrenz.lths.appointment.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

	private List<AllAppointmentDTO> data;

	private PageDTO page;
}
