package com.lifetrenz.lths.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Prasanna.M
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditEventAppoinmentDto {
	private AuditEventType eventType;
	private TokenPayLoad tokenPayLoad;
	private String data;
}
