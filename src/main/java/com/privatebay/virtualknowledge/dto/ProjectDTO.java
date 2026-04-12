package com.privatebay.virtualknowledge.dto;

import java.time.LocalDate;

public class ProjectDTO {
	private Long id;
	private String name;
	private String description;
	private LocalDate startDate;
	private LocalDate endDate;

	public ProjectDTO(Long id, String name, String description, LocalDate startDate, LocalDate endDate) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

}
