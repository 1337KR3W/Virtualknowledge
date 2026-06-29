package com.privatebay.virtualknowledge.dto;

import java.util.Map;

public class ProjectTimeRowDTO {

	private Long pid;
	private String projectName;
	private String departmentName; // <-- Añadimos el nuevo campo para capturar el valor del Front
	private Map<String, TimeEntryDTO> days;

	public ProjectTimeRowDTO() {
		super();
	}

	public ProjectTimeRowDTO(Long pid, String projectName, String departmentName, Map<String, TimeEntryDTO> days) {
		super();
		this.pid = pid;
		this.projectName = projectName;
		this.departmentName = departmentName;
		this.days = days;
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