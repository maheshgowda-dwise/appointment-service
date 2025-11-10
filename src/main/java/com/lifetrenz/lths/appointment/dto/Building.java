package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

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
public class Building implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private SystemMasterVO locationType;

	@NotNull
	private String name;

	private LocationSite customerBusinessSite;

	@NotNull
	private String identifierCode;

	private String aliasName;

	private String description;

	private SystemMasterVO locationStatus;

}
