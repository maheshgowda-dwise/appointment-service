package com.lifetrenz.lths.appointment.dto;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerMasterDTO {

	private Long id;

	private String nameEn;

	private String description;

	private String identifierCode;




}
