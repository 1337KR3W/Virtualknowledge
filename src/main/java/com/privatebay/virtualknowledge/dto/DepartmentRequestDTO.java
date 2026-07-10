package com.privatebay.virtualknowledge.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Necessary data in order to create or update a department")
public class DepartmentRequestDTO {
	
	@Schema(description = "Department name", example = "Development", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank
	@Size(min = 3, max = 100)
    private String name;
    
	@Schema(description = "List of users IDs to be assigned to a department", example = "[1, 2, 3]")
    private List<Long> userIds;

    public DepartmentRequestDTO() {
    }
    
    
    public DepartmentRequestDTO(@NotBlank @Size(min = 3, max = 100) String name) {
		super();
		this.name = name;
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