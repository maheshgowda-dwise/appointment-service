/**
 * 
 */
package com.lifetrenz.lths.appointment.model.value_object;

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
public class UserSiteRole {

	/**
	 * 
	 */
	private String id;
	private String name;
	private String description;
	private Boolean standard;
	private String identifier;
	private Boolean isDefault;
	private String roleGroup;
	private String roleGroupName;
	private Long roleGroupId;
	private String roleGroupIdentifier;

}
