package com.lifetrenz.lths.appointment.model.value_object;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Ajith.K
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemMasterNew implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private String nameEn;

	private String description;

	private String identifierCode;

	private String typeId;

	private String typeIdentifier;

}
