package com.lifetrenz.lths.appointment.model.value_object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClinicalItem {

	private String itemId;

	private String itemName;

	private String itemIdentifier;
}
