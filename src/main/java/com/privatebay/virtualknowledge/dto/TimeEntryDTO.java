package com.privatebay.virtualknowledge.dto;

import java.math.BigDecimal;

public class TimeEntryDTO {
	private BigDecimal hours;
	private String comment;

	public TimeEntryDTO() {
		super();
	}

	public TimeEntryDTO(BigDecimal hours, String comment) {
		super();
		this.hours = hours;
		this.comment = comment;
	}

	public BigDecimal getHours() {
		return hours;
	}

	public void setHours(BigDecimal hours) {
		this.hours = hours;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
