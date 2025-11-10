package com.lifetrenz.lths.appointment.repository.command;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.lifetrenz.lths.appointment.enums.FilterComponentType;
import com.lifetrenz.lths.appointment.model.collection.ComponentFilter;


@Repository
public interface IComponentFilterRepository extends MongoRepository<ComponentFilter, String> {

	List<ComponentFilter> findByFilterUserId(Long filterUserId);


	List<ComponentFilter> findByComponentTypeAndFilterUserIdAndIsDefaultAndCustomerTransactionAttribute_CustomerBusinessId(
			FilterComponentType type, Long filterUserId, Boolean isDefault, Long customerBusinessId);

}
