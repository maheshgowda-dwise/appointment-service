package com.lifetrenz.lths.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Kranthi.K
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOrderItemDto {

	private Long id;

	private Float qty;

	private Long appointmentId;

	private String orderingPhysicianId;

	private String orderingPhysicianName;

	private String physicianSpeciality;

	private String fullOrderText;

	private String orderName;

	private String catalogueCategoryIdentifier;

	private String catalogueCategoryName;

	private Long catalogueItemId;
	private String catalogueItemIdentifier;

	private String clinicalCommonNameIdentifier;

	private CustomerTransactionAttributeDTO customerTransactionAttribute;
	
	private Boolean isPackageServiceOrder;
	

}
