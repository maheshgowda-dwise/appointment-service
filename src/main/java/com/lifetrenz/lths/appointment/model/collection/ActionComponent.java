/**
 * 
 */
package com.lifetrenz.lths.appointment.model.collection;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.lifetrenz.lths.appointment.model.value_object.ActionComponentAccess;
import com.lifetrenz.lths.appointment.model.value_object.TransactionBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Ajith.K
 *
 */
@Document(collection = "action_components")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class ActionComponent extends TransactionBase{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	@NotNull
	private String name;
	
	private String description;
	
	@NotNull
	private String identifierCode;
	
	private List<ActionComponentAccess> access;
	
}
