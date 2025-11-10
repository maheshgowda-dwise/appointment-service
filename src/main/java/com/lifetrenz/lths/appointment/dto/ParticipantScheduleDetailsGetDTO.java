package com.lifetrenz.lths.appointment.dto;

import org.springframework.data.domain.Page;

import com.lifetrenz.lths.appointment.model.collection.ParticipantScheduleDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantScheduleDetailsGetDTO {

	private Page<ParticipantScheduleDetails> data;

	private PageDTO page;

}
