package com.lifetrenz.lths.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PageDTO {
	private int totalPages;
	private int currentPageNo;
	private int currentPageSize;
	private Long total;
}
