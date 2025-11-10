package com.lifetrenz.lths.appointment.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifetrenz.lths.appointment.common.enums.DayOfWeek;
import com.lifetrenz.lths.appointment.dto.TimeDto;

/**
 * @author Ajith.K
 *
 */
public class AppUtil {

	public static Date convertEpochToDate1(Long epochDate) {
		return new Date(epochDate * 1000);
	}

	public static Date convertEpochToDate(Long value) {
		LocalDateTime ldt = Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDateTime();

		return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate getLocalDate(Long epochMilliSeconds) {
		LocalDate ld = Instant.ofEpochMilli(epochMilliSeconds).atZone(ZoneId.systemDefault()).toLocalDate();
		return ld;
	}

	public static String getDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

//		DayOfWeek ds = DayOfWeek.values()[cal.get(Calendar.DAY_OF_WEEK)-1];
		String str = DayOfWeek.values()[cal.get(Calendar.DAY_OF_WEEK) - 1].value;

		return str;
	}

	public static long getTimeDifferenceInMinutes(String time1, String time2) {
		if (time1 == null || time2 == null) {
			return 0;
		}

		String[] arrTime1 = time1.split(":");
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new Date());
		cal1.set(Calendar.HOUR_OF_DAY, Integer.valueOf(arrTime1[0]));
		cal1.set(Calendar.MINUTE, Integer.valueOf(arrTime1[1]));
		cal1.set(Calendar.SECOND, 0);

		String[] arrTime2 = time2.split(":");
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(new Date());
		cal2.set(Calendar.HOUR_OF_DAY, Integer.valueOf(arrTime2[0]));
		cal2.set(Calendar.MINUTE, Integer.valueOf(arrTime2[1]));
		cal2.set(Calendar.SECOND, 0);
		long diff = cal2.getTimeInMillis() - cal1.getTimeInMillis();
		long diffMinutes = diff / (60 * 1000);

		return diffMinutes;
	}

	public static Calendar getCurrentTime(String time) {
		String[] arrTime = time.split(":");
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new Date());
		cal1.set(Calendar.HOUR_OF_DAY, Integer.valueOf(arrTime[0]));
		cal1.set(Calendar.MINUTE, Integer.valueOf(arrTime[1]));
		cal1.set(Calendar.SECOND, 0);
		return cal1;
	}

	public static LocalTime getLocalTime(Calendar calendar, int duration) {
		calendar.add(Calendar.MINUTE, duration);
//		String value = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
		Instant instant = calendar.toInstant();
		ZoneId zoneId = TimeZone.getDefault().toZoneId();
		LocalTime localTime = LocalTime.ofInstant(instant, zoneId);
		return localTime;
	}

	public static String convertJsonToString(Object request) {
		String finalValue = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			finalValue = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
		} catch (JsonProcessingException e) {
			finalValue = "";
		}
		return finalValue;
	}

	/**
	 * checks whether string is null
	 * 
	 * @param value
	 * @return true or false
	 */
	public static boolean isNullString(String value) {
		boolean isNull = false;
		if (value == null) {
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
		} else if (value.equals("undefined")) {
			isNull = true;
		}
		return isNull;
	}

	public static List<String> convertToList(String[] request) {
		List<String> strings = Arrays.asList(request);
		return strings;
	}

	public static long getTimeDifferenceByDate(Date time1, Date time2) {
		if (time1 == null || time2 == null) {
			return 0;
		}

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(time1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(time2);
		long diff = cal2.getTimeInMillis() - cal1.getTimeInMillis();
		long diffMinutes = diff / (60 * 1000);

		return diffMinutes;
	}

	public static String getRecurrenceException(Date recDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(recDate);

		String excption = "";
		int month = cal.get(Calendar.MONTH) + 1;

		excption = cal.get(Calendar.YEAR) + "" + (month > 9 ? month : ("0" + month)) + ""
				+ (cal.get(Calendar.DATE) > 9 ? cal.get(Calendar.DATE) : ("0" + cal.get(Calendar.DATE))) + "T"
				+ (cal.get(Calendar.HOUR) > 9 ? cal.get(Calendar.HOUR) : ("0") + cal.get(Calendar.HOUR)) + ""
				+ (cal.get(Calendar.MINUTE) > 9 ? cal.get(Calendar.MINUTE) : ("0") + cal.get(Calendar.MINUTE)) + "00Z";

		return excption;

	}

	public static String calculateAge(String dobString) {
		if (dobString != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate dob = LocalDate.parse(dobString, formatter);
			LocalDate currentDate = LocalDate.now();
			Period age = Period.between(dob, currentDate);

			int years = age.getYears();
			int months = age.getMonths();
			int days = age.getDays();

			// Construct the age string
			StringBuilder ageString = new StringBuilder();
			if (years > 0) {
				ageString.append(years).append(" Y, ");
			}
			if (months >= 0) {
				ageString.append(months).append(" M, ");
			}
			if (days >= 0) {
				ageString.append(days).append(" D");
			}

			return ageString.toString();
		} else {
			return null;
		}
	}

	public static LocalDate dateToLocalDate(Date date) {

		if (date != null) {
			Instant instant = date.toInstant(); // Convert Date to Instant
			return instant.atZone(ZoneId.systemDefault()).toLocalDate(); // Convert Instant to LocalDate
		} else {
			return null;
		}

	}

	public static String dateToString(Date date) {
		if (date != null) {
			LocalDate localDate = dateToLocalDate(date);
			return localDate.toString(); // Returns the string representation of LocalDate
		} else {
			return null;
		}
	}

	public static TimeDto getTime(String time) {
		if (time == null) {
			return null;
		}
		String[] times = time.split(":");
		if (times != null && times.length > 0) {
			return new TimeDto(Integer.valueOf(times[0]), Integer.valueOf(times[1]));
		}
		return null;
	}
	
	 public static Date parseDateTime(String dateTimeString, String format) {
	        if (dateTimeString == null || dateTimeString.isEmpty()) {
	            return null; // Return null if the input is null or empty
	        }

	        try {
	            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
	            dateFormat.setLenient(false); // Strict parsing
	            return dateFormat.parse(dateTimeString);
	        } catch (ParseException e) {
	            e.printStackTrace(); // Log the exception or handle it accordingly
	            return null; // Return null if parsing fails
	        }
	    }

}
