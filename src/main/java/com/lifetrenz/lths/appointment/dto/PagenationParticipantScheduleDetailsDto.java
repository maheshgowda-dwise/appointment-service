package com.lifetrenz.lths.appointment.dto;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.model.collection.ParticipantScheduleDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagenationParticipantScheduleDetailsDto {
	public Page<ParticipantScheduleDetails> userDTO;
	public PageDTO pageDTO;

}