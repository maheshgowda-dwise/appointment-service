/**
 * 
 */
package com.lifetrenz.lths.appointment.model.events;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.lifetrenz.lths.appointment.common.enums.MessageEventStatus;
import com.lifetrenz.lths.appointment.common.enums.MessageRequestStatus;
import com.lifetrenz.lths.appointment.dto.CustomerTransactionAttributeDTO;

import jakarta.validation.constraints.NotNull;
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
@Document(collection = "message_event")
public class MessageEvent {

	@Id
	private String id;

	private CustomerTransactionAttributeDTO transactionBase;

	private Integer key;

	private String topic;

	private Object request;

	@NotNull
	private MessageEventStatus eventStatus;

	private String errorMessage;

	private MessageRequestStatus status;

	private String source;
}
