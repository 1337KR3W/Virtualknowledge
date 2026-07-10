package com.privatebay.virtualknowledge.dto;

import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represent time rows for specific project in a timesheet")
public class ProjectTimeRowDTO {

	@Schema(description = "Project ID", example = "1")
	private Long pid;
	
	@Schema(description = "Project name", example = "Alfa Project")
	private String projectName;
	
	@Schema(description = "Name of the project's department", example = "Development")
	private String departmentName;
	
	@Schema(
		    description = "Time entry Map. Keys are days (ex: 'monday', 'tuesday') and values are objects with registered time.",
		    example = "{\"monday\": {\"hours\": 8.0, \"comment\": \"API development\"}, \"tuesday\": {\"hours\": 4.0, \"comment\": \"Team meeting\"}}"
		)
	private Map<String, TimeEntryDTO> days = new HashMap<>();

	public ProjectTimeRowDTO() {
		super();
	}

	public ProjectTimeRowDTO(Long pid, String projectName, String departmentName) {
		this.pid = pid;
		this.projectName = projectName;
		this.departmentName = departmentName;
	}

	public ProjectTimeRowDTO(Long pid, String projectName, String departmentName, Map<String, TimeEntryDTO> days) {
		super();
		this.pid = pid;
		this.projectName = projectName;
		this.departmentName = departmentName;
		this.days = days;
	}

	public void addEntry(String dayKey, TimeEntryDTO entry) {
		this.days.put(dayKey.toLowerCase(), entry);
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Map<String, TimeEntryDTO> getDays() {
		return days;
	}

	public void setDays(Map<String, TimeEntryDTO> days) {
		this.days = days;
	}

}