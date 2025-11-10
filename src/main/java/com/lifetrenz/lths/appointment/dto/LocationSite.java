package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;

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
public class LocationSite implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
}
