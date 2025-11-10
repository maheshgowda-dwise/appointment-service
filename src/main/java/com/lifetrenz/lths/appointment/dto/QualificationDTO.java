package com.lifetrenz.lths.appointment.dto;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualificationDTO {

	private String qualification;

	private String educationLevel;

	private String course;

	private Date date;

}
