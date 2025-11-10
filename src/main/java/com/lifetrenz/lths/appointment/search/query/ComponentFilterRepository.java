package com.lifetrenz.lths.appointment.search.query;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.lifetrenz.lths.appointment.dto.CustomerTransactionAttributeDTO;
import com.lifetrenz.lths.appointment.model.collection.ComponentFilter;
import com.lifetrenz.lths.appointment.search.dto.ComponentFilterDto;

@Repository
public class ComponentFilterRepository {

	    @Autowired
	    private MongoTemplate mongoTemplate;


	    public List<ComponentFilterDto> getComponentFilter(String type, String level, Long userId, Long siteId,
	            Long customerBusinessId) {

	        Query query = new Query();

	        if (type != null) {
	            query.addCriteria(Criteria.where("componentType").is(type));
	        }
	        if (level != null) {
	            query.addCriteria(Criteria.where("level").is(level));
	        }
	        if (userId != null) {
	            query.addCriteria(Criteria.where("filterUserId").is(userId));
	        }
	        if (siteId != null) {
	            query.addCriteria(Criteria.where("filterSiteId").is(siteId));
	        }
	        if (customerBusinessId != null) {
	            query.addCriteria(Criteria.where("customerTransactionAttribute.customerBusinessId").is(customerBusinessId));
	        }

	        query.addCriteria(Criteria.where("customerTransactionAttribute.active").is(true));
	        
	        List<ComponentFilter> filters = mongoTemplate.find(query, ComponentFilter.class);

	        return filters.stream()
	                .map(this::mapToDto)
	                .collect(Collectors.toList());
	    }


	    private ComponentFilterDto mapToDto(ComponentFilter filter) {
	    	
	    	CustomerTransactionAttributeDTO cb = new CustomerTransactionAttributeDTO(filter.getCustomerTransactionAttribute().getActive(),
	    			filter.getCustomerTransactionAttribute().getCreatedBy(), filter.getCustomerTransactionAttribute().getCreatedOn(),
	    			null, null, null, null, null, filter.getCustomerTransactionAttribute().getCreatedByName());

	        return new ComponentFilterDto(filter.getId(), cb, filter.getFilterName(), filter.getIsDefault(),
	        		filter.getComponentType(), filter.getLevel(), filter.getFormValue(), filter.getFilterSiteId(), filter.getFilterUserId());
	    }
}
