package com.lifetrenz.lths.appointment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdhocDTO {

	private Long locationId;

	private String locationName;

	private String doctorId;

	private String doctorName;

	private Long fromDateTime;

	private Long toDateTime;

}
