package com.privatebay.virtualknowledge.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Department detail data returned from API")
public class DepartmentResponseDTO {
	
	@Schema(description = "Unique department identifier", example = "1")
	private Long id;
	
	@Schema(description = "Department name", example = "Development")
	private String name;
	
	@Schema(description = "List of user IDs who belong to the department", example = "[1, 2, 3]")
	private List<Long> userIds;

	public DepartmentResponseDTO() {
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

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
}