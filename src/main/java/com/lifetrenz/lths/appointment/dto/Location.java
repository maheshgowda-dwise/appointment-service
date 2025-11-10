package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

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
public class Location implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private Long coreLocationId;

	private Building building;

	private Floor floor;

	private Unit unit;

	private Room room;

	private Department department;

	private TransactionBase transactionBase;

}
