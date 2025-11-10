package com.lifetrenz.lths.appointment.search.mapper;

import org.springframework.stereotype.Component;

import com.lifetrenz.lths.appointment.dto.CustomerTransactionAttributeDTO;
import com.lifetrenz.lths.appointment.model.collection.ComponentFilter;
import com.lifetrenz.lths.appointment.search.dto.ComponentFilterDto;

@Component
public class ComponentFilterMapper {

	public ComponentFilter mapComponentFilterDtoToEntity(ComponentFilterDto filter) {

		ComponentFilter componentFilter = new ComponentFilter(null, filter.getCustomerTransactionAttribute(), filter.getFilterName(), filter.getIsDefault(),
				filter.getComponentType(), filter.getLevel(), filter.getFormValue(), filter.getFilterSiteId(),
				filter.getFilterUserId());

		return componentFilter;

	}

	public ComponentFilterDto mapComponentFilterEntityToDto(ComponentFilter componentFilter) {

		CustomerTransactionAttributeDTO cb = new CustomerTransactionAttributeDTO();
		cb.setActive(componentFilter.getCustomerTransactionAttribute().getActive());
		cb.setCreatedBy(componentFilter.getCustomerTransactionAttribute().getCreatedBy());
		cb.setCreatedOn(componentFilter.getCustomerTransactionAttribute().getCreatedOn());

		ComponentFilterDto res = new ComponentFilterDto(componentFilter.getId(), cb, componentFilter.getFilterName(),
				componentFilter.getIsDefault(), componentFilter.getComponentType(), componentFilter.getLevel(),
				componentFilter.getFormValue(), componentFilter.getFilterSiteId(), componentFilter.getFilterUserId());
		return res;

	}
}
