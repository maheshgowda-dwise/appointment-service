package com.lifetrenz.lths.appointment.enums;
/** 
*
* @author Sai.KVSS
*
*/
public enum ReferralType {
	INTRA_REFERRAL ("INTRA-REFERRAL"),
	INTER_REFERRAL("INTER-REFERRAL"),
	EXTERNAL_REFERRAL("EXTERNAL_REFERRAL");

	public final String value;
	
	/**
	 * @param string
	 */
	ReferralType(String string) {
		value = string;
	}

}
