package com.privatebay.virtualknowledge.service;

import com.privatebay.virtualknowledge.dto.ProjectDTO;
import com.privatebay.virtualknowledge.entity.Project;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.repository.ProjectRepository;
import com.privatebay.virtualknowledge.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;

	public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
		this.projectRepository = projectRepository;
		this.userRepository = userRepository;
	}

	public List<ProjectDTO> findProjectsByUserId(Long userId) {
		List<Project> projects = projectRepository.findByUserId(userId);
		return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public List<ProjectDTO> getProjectsForWeek(Long userId, String weekId) {
		String[] parts = weekId.split("-W");
		int year = Integer.parseInt(parts[0]);
		int week = Integer.parseInt(parts[1]);

		LocalDate weekStart = LocalDate.of(year, 1, 1).with(java.time.temporal.WeekFields.ISO.weekOfYear(), week)
				.with(java.time.temporal.WeekFields.ISO.dayOfWeek(), 1);

		LocalDate weekEnd = weekStart.plusDays(6);

		List<Project> projects = projectRepository.findActiveProjectsInWeek(userId, weekStart, weekEnd);
		return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public Project createProject(Project project) {
		User user = userRepository.findById(project.getUser().getId())
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		boolean belongsToDepartment = user.getDepartments().stream()
				.anyMatch(dept -> dept.getId().equals(project.getDepartment().getId()));

		if (!belongsToDepartment) {
			throw new IllegalArgumentException("El usuario asignado no pertenece al departamento del proyecto.");
		}

		return projectRepository.save(project);
	}

	private ProjectDTO convertToDTO(Project project) {
		return new ProjectDTO(project.getId(), project.getName(), project.getDescription(), project.getStartDate(),
				project.getEndDate(), project.getDepartment().getId(), project.getDepartment().getName());
	}
}