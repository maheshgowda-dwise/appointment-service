package com.lifetrenz.lths.appointment.search.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.search.dto.ComponentFilterDto;

@Service
public interface ComponentFilterService {

	/**
	 * To save component filter
	 * 
	 * @param filter
	 * @return
	 * @throws ApplicationException
	 */
	ComponentFilterDto saveComponentFilter(ComponentFilterDto filter,TokenPayLoad tokenPayLoad, String path) throws ApplicationException;

	/**
	 * To get filter
	 * 
	 * @param type
	 * @param level
	 * @param userId
	 * @param siteId
	 * @param customerBusinessId
	 * @return
	 * @throws ApplicationException
	 */
	List<ComponentFilterDto> getComponentFilter(String type, String level, Long userId, Long siteId,
			Long customerBusinessId, String path) throws ApplicationException;

	/**
	 * To update default filter
	 * 
	 * @param filter
	 * @return
	 * @throws ApplicationException
	 */
	ComponentFilterDto updateDefaultFilter(ComponentFilterDto filter, String path) throws ApplicationException;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws ApplicationException
	 */
	ComponentFilterDto deleteFilter(String id) throws ApplicationException;
	
	/**
	 * 
	 * @param filter
	 * @return
	 * @throws ApplicationException
	 */
	ComponentFilterDto updateComponentFilter(ComponentFilterDto filter, String path) throws ApplicationException;

}
