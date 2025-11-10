package com.lifetrenz.lths.appointment.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AppUtils {

	public static Date convertEpochToDate(Long value) {
		LocalDateTime ldt = Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDateTime();

		return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 
	 * checks whether string is null
	 * 
	 * @param value
	 * @return true or false
	 */
	public static boolean isNullString(String value) {
		boolean isNull = false;
//		if (value.isEmpty()) {
//			isNull = true;
//		} else if (value.isBlank()) {
//			isNull = true;
//		} else if (value.equals(null)) {
//			isNull = true;
//		} else if (value.equals("null")) {
//			isNull = true;
//		}
//		return isNull;
		
		if (value == null) {
			isNull = true;
		} else if (value == "undefined") {
			isNull = true;
		} else if (value.equalsIgnoreCase("NA")) {
			isNull = true;
		} else if (value.isEmpty()) {
			isNull = true;
		} else if (value.isBlank()) {
			isNull = true;
		} else if (value.equals("null")) {
			isNull = true;
		} else if (value.equals("NULL")) {
			isNull = true;
		} else if (value.equals("Null")) {
			isNull = true;
		}
		return isNull;
	}
	
	public static String convertJsonToString(Object request) {
		String finalValue = "";
		 ObjectMapper Obj = new ObjectMapper();
		 try {
			 finalValue = Obj.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			finalValue = "";
		}
		return finalValue;
	}

}
