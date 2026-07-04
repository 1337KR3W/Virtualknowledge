package com.privatebay.virtualknowledge.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TimeSheetRequestDTO {
	
	@Size(max = 10)
	private String weekId;
	
	private List<LocalDate> weekDates;
	
	@Size(max = 500)
	private String globalComment;
	
	private List<ProjectTimeRowDTO> rows;
	
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
