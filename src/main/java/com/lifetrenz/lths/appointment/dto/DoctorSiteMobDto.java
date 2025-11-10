package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DoctorSiteMobDto implements Serializable {

	private static final long serialVersionUID = 8761982209028311286L;

	private String id;

	private String name;

	private TelecomDTO telecom;

	private Boolean isDefault;

	private String address;

	private String siteAvailable;

	private List<ScheduleMobDto> schedules;

	private String imageUrl;

	private Boolean isImageUploaded;

	private Long customerBusinessId;

	private String customerBusinessName;

	private Long customerId;

	private String customerName;

}
