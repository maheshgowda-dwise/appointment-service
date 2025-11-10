/**
 * 
 */
package com.lifetrenz.lths.appointment.dto;

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
public class AppointmentStatusDTO {

	private String[] referenceAppointmentIds;

	private String apntStatusIdentifierCode;

	private String journeyStatusIdentifierCode;

	private String updatedBy;

	private Long updatedById;

}
