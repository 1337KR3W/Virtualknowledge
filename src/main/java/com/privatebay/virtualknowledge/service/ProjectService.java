package com.privatebay.virtualknowledge.service;

import com.privatebay.virtualknowledge.dto.ProjectDTO;
import com.privatebay.virtualknowledge.entity.Project;
import com.privatebay.virtualknowledge.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

	private final ProjectRepository projectRepository;

	public ProjectService(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	public List<ProjectDTO> findProjectsByUserId(Long userId) {
		List<Project> projects = projectRepository.findByUserId(userId);
		return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private ProjectDTO convertToDTO(Project project) {
		return new ProjectDTO(project.getId(), project.getName(), project.getDescription(), project.getStartDate(), project.getEndDate());
	}
	
	public List<ProjectDTO> getProjectsForWeek(Long userId, String weekId) {

	    String[] parts = weekId.split("-W");
	    int year = Integer.parseInt(parts[0]);
	    int week = Integer.parseInt(parts[1]);

	    LocalDate weekStart = LocalDate.of(year, 1, 1)
	            .with(java.time.temporal.WeekFields.ISO.weekOfYear(), week)
	            .with(java.time.temporal.WeekFields.ISO.dayOfWeek(), 1);
	    
	    LocalDate weekEnd = weekStart.plusDays(6);

	    List<Project> projects = projectRepository.findActiveProjectsInWeek(userId, weekStart, weekEnd);

	    return projects.stream().map(p -> new ProjectDTO(
	        p.getId(), 
	        p.getName(),
	        p.getDescription(),
	        p.getStartDate(), 
	        p.getEndDate()
	    )).collect(Collectors.toList());
	}
}
