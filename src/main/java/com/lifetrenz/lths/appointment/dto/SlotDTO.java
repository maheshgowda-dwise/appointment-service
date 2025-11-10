package com.lifetrenz.lths.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotDTO {

	private String Slotid;

	private Long participantId;

	private String aptConfigId;

	private String participantName;

	private String slotStatus;

	private String intervel;

	private String avilablefromTime;

	private String avilabletoTime;

}
