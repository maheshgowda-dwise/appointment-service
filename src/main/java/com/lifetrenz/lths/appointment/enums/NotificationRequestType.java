package com.lifetrenz.lths.appointment.enums;

public enum NotificationRequestType {

	BOOK_APPOINTMENT("BOOK-APNT"), CANCEL_APPOINTMENT("CANCEL-APNT"), CONSULT_COMPLETE("Consult-CMPLTE"),
	MARK_ARRIVE("MARK_ARRIVE"), REPORT("RPT"), OPD_RESCHEDULING("OPD-RES"), FOLLOW_UP_OPD("OPD-FOLUP"),
	OPD_TIME_ALTERATION("OPD-TIMALT"), IP_BOOKING("IP-BOK"), IP_BOOKING_REMINDER("IP-BOKREM"),
	OPD_REMINDER("OPD-BOKREM"), LAB_ORDERS("LAB-ORD"), LAB_REPORTS("LAB-REP"), OPD_BILL_PAYMENT("OPD-BILPAY");

	public final String value;

	private NotificationRequestType(String value) {
		this.value = value;
	}

}
