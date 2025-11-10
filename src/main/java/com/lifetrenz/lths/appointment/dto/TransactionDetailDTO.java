package com.lifetrenz.lths.appointment.dto;

import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDetailDTO {
	
	@NotNull
	  private Long customerId;
	  
	  @NotNull
	  private Long customerBusinessId;
	  
	  @NotNull
	  private Long siteId;
	  
	  private Long userId;
	  
	  @NotNull
	  private String createdBy;
	  
	  @NotNull
	  private Long createdById;
	  
	  @NotNull
	  private Date createdOn;
	  
	  private String updatedBy;
	  
	  private Long updatedById;
	  
	  private Date updatedOn;
	  
}
