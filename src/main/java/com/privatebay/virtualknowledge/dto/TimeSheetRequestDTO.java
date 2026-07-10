package com.privatebay.virtualknowledge.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Necessary data in order to create or update a timesheet")
public class TimeSheetRequestDTO {
	
	@Schema(description = "Week ID", example = "2026-W28")
	@Size(max = 10)
	private String weekId;
	
	@Schema(description = "List of corresponding dates from week", example = "[\"2026-07-06\", \"2026-07-07\", \"2026-07-08\"]")
	private List<LocalDate> weekDates;
	
	@Schema(description = "Weekly global comment", example = "I'm on holidays during the week.")
	@Size(max = 500)
	private String globalComment;
	
	@Schema(description = "List of project records")
	private List<ProjectTimeRowDTO> rows;
	
	@Schema(description = "Timesheet user ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull
	private Long userId;

	public TimeSheetRequestDTO() {
		super();
	}

	public TimeSheetRequestDTO(String weekId, String globalComment, List<ProjectTimeRowDTO> rows, Long userId) {
		super();
		this.weekId = weekId;
		this.globalComment = globalComment;
		this.rows = rows;
		this.userId = userId;
	}

	public String getWeekId() {
		return weekId;
	}

	public void setWeekId(String weekId) {
		this.weekId = weekId;
	}

	public String getGlobalComment() {
		return globalComment;
	}

	public void setGlobalComment(String globalComment) {
		this.globalComment = globalComment;
	}

	public List<ProjectTimeRowDTO> getRows() {
		return rows;
	}

	public void setRows(List<ProjectTimeRowDTO> rows) {
		this.rows = rows;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<LocalDate> getWeekDates() {
		return weekDates;
	}

	public void setWeekDates(List<LocalDate> weekDates) {
		this.weekDates = weekDates;
	}

}
