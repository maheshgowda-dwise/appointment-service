package com.lifetrenz.lths.appointment.service;

import org.springframework.stereotype.Service;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.dto.CustomerTransactionAttributeDTO;
import com.lifetrenz.lths.appointment.dto.LtAuditEventDto;




@Service
public interface AuditService {
	
	/**
	 * 
	 * @param request
	 * @param custAttr
	 * @throws ApplicationException
	 */
	public void addtoAudit(LtAuditEventDto request, CustomerTransactionAttributeDTO custAttr, String path) throws ApplicationException;
}