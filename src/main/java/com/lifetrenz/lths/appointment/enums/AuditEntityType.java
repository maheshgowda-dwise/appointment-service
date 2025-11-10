package com.lifetrenz.lths.appointment.enums;

public enum AuditEntityType {

	PATIENT("patient"),
	SETTINGS("settings"),
	CUSTOMER_MASTER("customer_master"),
	EQUIPMENT("equipment"),
	REASON("reason"),
	UOM("unit_of_measure"),
	CUSTOMER_BUSINESS_SITE("customer_business_site");
	
	public final String value;

	/**
	 * 
	 * @param i
	 */
	AuditEntityType(String i) {
		this.value = i;
	}
}
