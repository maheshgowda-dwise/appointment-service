/**
 * 
 */
package com.lifetrenz.lths.appointment.model.value_object;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.dto.KafkaUMSTelecom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jess.B
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSite {

	/**
	 * 
	 */
	private int id;
	private String name;
	private String aliasName;
	private String identifier;
	private List<UserSiteRole> roles;
	private Location location;
	private Boolean isDefault;
	private String address;
	private String timeZone;
	private Long countryId;
	private Long stateId;
	private Long cityId;
	private Long customerBusinessId;
	private String customerBusinessName;
	private Long customerId;
	private String customerName;
	private String availability;
	private KafkaUMSTelecom telecom;




}
