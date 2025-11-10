package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LTMBookAppointmentDto implements Serializable {

	private static final long serialVersionUID = 5519398023958659883L;

	OldAppointmentDto bookRequest;
	TokenPayLoad tokenPayload;
	CustomerTransactionBase customerTransactionBase;

}
