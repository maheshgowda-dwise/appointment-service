/**
 * 
 */
package com.lifetrenz.lths.appointment.model.value_object;

import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.lifetrenz.lths.appointment.model.collection.CheckListStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ajith.K
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkArriveCheckList {

	@NotNull
	private String checkListId;

	private CheckListStatus status;
	
	private Date expiryDate;
}
