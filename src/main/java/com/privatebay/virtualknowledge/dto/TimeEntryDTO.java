package com.privatebay.virtualknowledge.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Specific day entry details")
public class TimeEntryDTO {
	
	@Schema(description = "Total hours in a single entry day", example = "8.5", requiredMode = Schema.RequiredMode.REQUIRED)
	private BigDecimal hours;
	
	@Schema(description = "Descriptive comment for a day entry", example = "Endpoint development with Spring Boot")
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
