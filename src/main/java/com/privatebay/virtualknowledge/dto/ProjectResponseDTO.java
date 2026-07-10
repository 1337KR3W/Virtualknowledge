package com.privatebay.virtualknowledge.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Project detail data returned from API")
public class ProjectResponseDTO {

	@Schema(description = "Unique project ID", example = "1")
	private Long id;

	@Schema(description = "Project name", example = "Alfa Project")
	private String name;

	@Schema(description = "Project description", example = "Alfa project is the new client request for ...")
	private String description;

	@Schema(description = "Project start date", example = "2026-07-10T09:00:00")
	private LocalDateTime startDate;
	
	@Schema(description = "Project end date", example = "2026-10-01T09:00:00")
	private LocalDateTime endDate;

	@Schema(description = "Reponsible department name", example = "Development")
	private String departmentName;

	@Schema(description = "Reponsible department ID", example = "1")
	private Long departmentId;

	@Schema(description = "List of user IDs assigned to a project", example = "[10, 11, 12]")
	private List<Long> userIds;

	public ProjectResponseDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
}