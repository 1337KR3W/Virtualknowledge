package com.privatebay.virtualknowledge.mapper;

import com.privatebay.virtualknowledge.dto.ProjectResponseDTO;
import com.privatebay.virtualknowledge.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

	public ProjectResponseDTO toResponseDTO(Project project) {
		if (project == null)
			return null;

		ProjectResponseDTO dto = new ProjectResponseDTO();
		dto.setId(project.getId());
		dto.setName(project.getName());
		dto.setDescription(project.getDescription());
		dto.setStartDate(project.getStartDate());
		dto.setEndDate(project.getEndDate());

		if (project.getDepartment() != null) {
			dto.setDepartmentName(project.getDepartment().getName());
		}

		return dto;
	}
}