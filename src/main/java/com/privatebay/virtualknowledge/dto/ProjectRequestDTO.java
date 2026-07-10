package com.privatebay.virtualknowledge.dto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Necessary data in order to create or update a project")
public class ProjectRequestDTO {
	
	@Schema(description = "Project name", example = "Alfa Project", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank
	@Size(min = 3, max = 150)
	private String name;
	
	@Schema(description = "Project description", example = "Alfa project is the new client request for ...", requiredMode = Schema.RequiredMode.REQUIRED)
	@Size(max = 500)
	private String description;
	
	@Schema(description = "Project start date", example = "2026-07-10T09:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull
	private LocalDateTime startDate;
	
	@Schema(description = "Project end date", example = "2026-10-01T09:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
	private LocalDateTime endDate;
	
	@Schema(description = "Reponsible department ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull
	private Long departmentId;
	
	@Schema(description = "List of user IDs assigned to a project", example = "[10, 11, 12]")
	private Set<Long> userIds = new HashSet<>();

	public ProjectRequestDTO() {
	}
	

	public ProjectRequestDTO(@NotBlank @Size(min = 3, max = 150) String name) {
		super();
		this.name = name;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Set<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(Set<Long> userIds) {
		this.userIds = userIds;
	}
}