/**
 * 
 */
package com.lifetrenz.lths.appointment.model.collection;

/**
 * @author Ajith.K
 *
 */
public enum CheckListStatus {

	PENDING("PENDING"), COMPLETED("COMPLETED");

	public String value;

	CheckListStatus(String value) {
		this.value = value;
	}
}
