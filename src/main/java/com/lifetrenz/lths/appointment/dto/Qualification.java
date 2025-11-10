package com.lifetrenz.lths.appointment.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Qualification implements Serializable{

	private static final long serialVersionUID = 9160493131387100646L;

	private String id;

	private String educationLevel;

	private String university;

	private String passingYear;

	private String degree;

}
