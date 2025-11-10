package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KafkaUMSTelecom implements Serializable{

	private static final long serialVersionUID = 454333410831274550L;
	
	private Long telecomType;
	private String telecomCode;
	private String telecomNumber;

}
