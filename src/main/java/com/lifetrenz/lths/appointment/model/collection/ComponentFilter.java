package com.lifetrenz.lths.appointment.model.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.lifetrenz.lths.appointment.dto.CustomerTransactionAttributeDTO;
import com.lifetrenz.lths.appointment.enums.FilterComponentType;
import com.lifetrenz.lths.appointment.enums.FilterLevel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "component_filter")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComponentFilter {

	@Id
	private String id;

	private CustomerTransactionAttributeDTO customerTransactionAttribute;

	private String filterName;

	private Boolean isDefault;

	private FilterComponentType componentType;

	private FilterLevel level;

	private String formValue;

	private Long filterSiteId;

	private Long filterUserId;

}