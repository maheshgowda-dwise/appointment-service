package com.lifetrenz.lths.appointment.dto;

/*
 * 
 * @author Prasanna.M
 * 
 */

public enum AuditEventType {

	LOGIN_DETAILS("LOGIN_DETAILS"), 
	LOGOUT_DETAILS("LOGOUT_DETAILS"),
	APPOINTMENT_CONFIGURATION("APPOINTMENT CONFIGURATION");
	public final String value;
	
	
	AuditEventType(String string){
		value=string;
	}
}
