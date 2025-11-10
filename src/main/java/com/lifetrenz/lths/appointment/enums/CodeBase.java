package com.lifetrenz.lths.appointment.enums;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public enum CodeBase {
    
	LT,
	HT
}
