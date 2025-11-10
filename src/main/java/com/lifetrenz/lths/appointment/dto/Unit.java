package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Sai.KVS
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Unit implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private LocationSite customerBusinessSite;

	private SystemMasterVO locationType;

	@JsonIgnore
	private Building building;

	@JsonIgnore
	private Floor floor;

	private Unit parentUnit;

	private SystemMasterVO type;

	@NotNull
	private String name;

	@NotNull
	private String identifierCode;

	private String aliasName;

	private String description;

	private SystemMasterVO locationStatus;

	private Boolean isStockArea;

	private Department department;

}