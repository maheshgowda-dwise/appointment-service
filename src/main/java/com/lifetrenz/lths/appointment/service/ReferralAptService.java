package com.lifetrenz.lths.appointment.service;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.dto.ReferralConsultationDTO;

/**
 *
 * @author Sai.KVSS
 *
 */
public interface ReferralAptService {

	/**
	 * This method is used to Save the Referral Appointments
	 * 
	 * @param referralConsultationDTO
	 * @return
	 * @throws ApplicationException
	 */
	public ReferralConsultationDTO saveReferralAppointments(ReferralConsultationDTO referralConsultationDTO)
			throws ApplicationException;

	/**
	 * This method is used to delete the Referraal Appointments
	 * 
	 * @param id
	 * @return
	 * @throws ApplicationException
	 */
	public Boolean deleteReferralAppointments(String referralId) throws ApplicationException;

}
