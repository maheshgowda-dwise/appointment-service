package com.lifetrenz.lths.appointment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatLocationDTO {
	private Long wardId;
	private String wardName;
	private Long roomId;
	private String roomName;
	private Long bedId;
	private  String bedName;
}
