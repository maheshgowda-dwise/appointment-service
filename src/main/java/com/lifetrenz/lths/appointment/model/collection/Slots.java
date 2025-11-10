package com.lifetrenz.lths.appointment.model.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 
 * @author Mujaheed.N
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "slots")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Slots {

	@Id
	private String id;

	private String aptConfigId;

	private Long participantId;

	private String participantName;

	private String slotStatus;

	private String intervel;

	private String avilablefromTime;

	private String avilabletoTime;


	
}
