package com.ultratendency.challenge;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * The class Day provides the date for a specific day. 
 * @author Soontaek Lim
 */
public class Day {
	LocalDate day;
	
	public Day(LocalDate day) {
		this.day = day;
	}
	
	public Date getEndOfDay() {
		LocalDateTime localDateTime = day.atStartOfDay();
		LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
		return localDateTimeToDate(endOfDay);
	}

	public Date getStartOfDay() {
		return localDateTimeToDate(day.atStartOfDay());
	}

	private Date localDateTimeToDate(LocalDateTime startOfDay) {
		return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
	}
}
