/**
 * 
 */
package com.lifetrenz.lths.appointment.model.value_object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Ajith.K
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerTransactionBase extends TransactionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long customerId;

	@NotNull
	private Long customerBusinessId;

	@NotNull
	private Long siteId;

}
