/**
 * 
 */
package com.lifetrenz.lths.appointment.common.enums;

/**
 * @author Ajith.K
 *
 */
public enum MessageEventStatus {
	INPROGRESS("INPROGRESS"),
	SUCCESS("SUCCESS"),
    FAILURE("FAILURE"),
    FAILED_ON_PUBLISH("FAILED_ON_PUBLISH"),
    FAILED_ON_CONSUME("FAILED_ON_CONSUME"),
    FAILED_ON_CODE("FAILED_ON_CODE");
    
    public final String value;

	/**
	 * @param i
	 */
    MessageEventStatus(String i) {
		this.value = i;
	}
}
