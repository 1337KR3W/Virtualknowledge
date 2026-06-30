package com.privatebay.virtualknowledge.service;

import com.privatebay.virtualknowledge.dto.ProjectCreateDTO;
import com.privatebay.virtualknowledge.dto.ProjectDTO;
import com.privatebay.virtualknowledge.entity.*;
import com.privatebay.virtualknowledge.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;
	private final DepartmentRepository departmentRepository;

	public ProjectService(ProjectRepository projectRepository, UserRepository userRepository,
			DepartmentRepository departmentRepository) {
		this.projectRepository = projectRepository;
		this.userRepository = userRepository;
		this.departmentRepository = departmentRepository;
	}

	public List<ProjectDTO> findProjectsByUserId(Long userId) {
		return projectRepository.findByUserId(userId).stream().map(this::convertToDTO).collect(Collectors.toList());
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

	@Transactional
	public Project createProject(ProjectCreateDTO dto) {
		Department dept = departmentRepository.findById(dto.getDepartmentId())
				.orElseThrow(() -> new IllegalArgumentException("Departamento no encontrado"));

		Project project = new Project();
		project.setName(dto.getName());
		project.setDescription(dto.getDescription());
		project.setStartDate(dto.getStartDate() != null ? dto.getStartDate() : LocalDate.now());
		project.setEndDate(dto.getEndDate());
		project.setDepartment(dept);

		Project savedProject = projectRepository.save(project);

		if (dto.getUserIds() != null && !dto.getUserIds().isEmpty()) {
			List<User> users = userRepository.findAllById(dto.getUserIds());
			savedProject.setUsers(new HashSet<>(users));
			return projectRepository.save(savedProject);
		}

		return savedProject;
	}

	public ProjectDTO convertToDTO(Project project) {
		return new ProjectDTO(project.getId(), project.getName(), project.getDescription(), project.getStartDate(),
				project.getEndDate(), project.getDepartment().getId(), project.getDepartment().getName());
	}
}