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
@AllArgsConstructor
@NoArgsConstructor
public class SystemMasterVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	@NotNull
	private String name;
	@NotNull
	private String identifierCode;

}
