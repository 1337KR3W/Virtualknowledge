package com.privatebay.virtualknowledge.service;

import com.privatebay.virtualknowledge.dto.ProjectRequestDTO;
import com.privatebay.virtualknowledge.dto.ProjectResponseDTO;
import com.privatebay.virtualknowledge.entity.*;
import com.privatebay.virtualknowledge.exception.ConflictException;
import com.privatebay.virtualknowledge.mapper.ProjectMapper;
import com.privatebay.virtualknowledge.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;
	private final DepartmentRepository departmentRepository;
	private final ProjectMapper projectMapper;

	public ProjectService(ProjectRepository projectRepository, UserRepository userRepository,
			DepartmentRepository departmentRepository, ProjectMapper projectMapper) {
		this.projectRepository = projectRepository;
		this.userRepository = userRepository;
		this.departmentRepository = departmentRepository;
		this.projectMapper = projectMapper;
	}

	@Transactional(readOnly = true)
	public List<ProjectResponseDTO> findAllProjects() {
		return projectRepository.findAll().stream().map(projectMapper::toResponseDTO).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ProjectResponseDTO getProjectById(Long id) {
		Project project = projectRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Project not found"));
		return projectMapper.toResponseDTO(project);
	}

	private void validateDates(LocalDateTime start, LocalDateTime end) {
        if (end != null && end.isBefore(start)) {
            throw new IllegalArgumentException("The end date must be later than the start date.");
        }
    }
	
	@Transactional
	public ProjectResponseDTO createProject(ProjectRequestDTO dto) {
		
		if (projectRepository.existsByName(dto.getName())) {
	        throw new ConflictException("A project with the name '" + dto.getName() + "' already exists.");
	    }
		
		validateDates(dto.getStartDate(), dto.getEndDate());
		
		Department dept = departmentRepository.findById(dto.getDepartmentId())
				.orElseThrow(() -> new IllegalArgumentException("Department not found"));

		Project project = new Project();
		project.setName(dto.getName());
		project.setDescription(dto.getDescription());
		project.setStartDate(dto.getStartDate());
		project.setEndDate(dto.getEndDate());
		project.setDepartment(dept);

		if (dto.getUserIds() != null) {
			project.setUsers(new HashSet<>(userRepository.findAllById(dto.getUserIds())));
		}

		return projectMapper.toResponseDTO(projectRepository.save(project));
	}

	@Transactional
	public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO dto) {
		
		validateDates(dto.getStartDate(), dto.getEndDate());
		
		Project project = projectRepository.findByIdWithUsers(id)
				.orElseThrow(() -> new RuntimeException("Project not found"));
		
		if (dto.getDepartmentId() == null) {
	        throw new IllegalArgumentException("Department ID is required");
	    }

		project.setName(dto.getName());
		project.setDescription(dto.getDescription());
		project.setStartDate(dto.getStartDate());
		project.setEndDate(dto.getEndDate());
		
		Department dept = departmentRepository.findById(dto.getDepartmentId())
	            .orElseThrow(() -> new RuntimeException("Department not found"));
	    project.setDepartment(dept);
		
		project.setDepartment(departmentRepository.findById(dto.getDepartmentId())
				.orElseThrow(() -> new RuntimeException("Department not found")));

		if (dto.getUserIds() != null) {
			project.setUsers(new HashSet<>(userRepository.findAllById(dto.getUserIds())));
		}

		return projectMapper.toResponseDTO(projectRepository.save(project));
	}

	public void deleteProject(Long id) {
		if (!projectRepository.existsById(id))
			throw new RuntimeException("Project not found");
		projectRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<ProjectResponseDTO> findProjectsByUserId(Long userId) {
		return projectRepository.findProjectByUserId(userId).stream().map(projectMapper::toResponseDTO)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<ProjectResponseDTO> getProjectsForWeek(Long userId, String weekId) {
		String[] parts = weekId.split("-W");
		int year = Integer.parseInt(parts[0]);
		int week = Integer.parseInt(parts[1]);

		LocalDate weekStart = LocalDate.of(year, 1, 1).with(WeekFields.ISO.weekOfYear(), week)
				.with(WeekFields.ISO.dayOfWeek(), 1);
		LocalDate weekEnd = weekStart.plusDays(6);

		List<Project> projects = projectRepository.findActiveProjectsInWeek(userId, weekStart, weekEnd);
		return projects.stream().map(projectMapper::toResponseDTO).collect(Collectors.toList());
	}
}