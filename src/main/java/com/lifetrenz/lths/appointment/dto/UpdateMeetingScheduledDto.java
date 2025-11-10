package com.lifetrenz.lths.appointment.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMeetingScheduledDto {
	
	private Long oldUserId;
	private Long userId;
	private String fullName;
	private List<ReferrenceIdDTO> referrenceIds;

}
