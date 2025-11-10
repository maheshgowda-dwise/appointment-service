package com.lifetrenz.lths.appointment.search.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.dto.CustomerTransactionAttributeDTO;
import com.lifetrenz.lths.appointment.enums.FilterComponentType;
import com.lifetrenz.lths.appointment.enums.FilterLevel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComponentFilterDto {

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
