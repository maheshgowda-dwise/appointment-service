package com.lifetrenz.lths.appointment.dto;

/**
 * @author Pratim.S
 */
public enum OPDEventType {
	SERVICE_ORDERS(0), NEED_CLOSE(1), UPDATE_VISIT_TYPE(2), CREATE_APPOINTMENT(3), CANCEL_APPOINTMENT(4),
	RESCHEDULE_APPOINTMENT(5), PAYMENT_APPOINTMENT(6), CANCEL_VISIT(7), UPDATE_TEMP_PATIENT(8),
	UPDATE_PAYMENT_STATUS(9);

	public final int value;

	/**
	 * @param i
	 */
	OPDEventType(int i) {
		this.value = i;
	}

}
