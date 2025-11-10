package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KafkaAppointmentDto implements Serializable {

	private static final long serialVersionUID = 6856860434144773659L;

	KafkaEventType kafkaType;

	AdmissionDashboardStatusDTO admissionDashboardStatus;
	
	KafkaAppointmentStatusDto appointmentStatus;

}
