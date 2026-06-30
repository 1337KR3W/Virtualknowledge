package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.dto.ProjectCreateDTO;
import com.privatebay.virtualknowledge.dto.ProjectDTO;
import com.privatebay.virtualknowledge.entity.Project;
import com.privatebay.virtualknowledge.service.ProjectService;
import com.privatebay.virtualknowledge.service.SecurityService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Importante
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

	private final ProjectService projectService;
	private final SecurityService securityService;

	public ProjectController(ProjectService projectService, SecurityService securityService) {
		this.projectService = projectService;
		this.securityService = securityService;
	}

	@GetMapping("/my-projects")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<List<ProjectDTO>> getAllProjects() {
		Long userId = securityService.getCurrentUserId();
		List<ProjectDTO> projects = projectService.findProjectsByUserId(userId);
		return ResponseEntity.ok(projects);
	}

	@GetMapping("/my-projects/week/{weekId}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<List<ProjectDTO>> getProjectsByWeek(@PathVariable String weekId) {

		Long userId = securityService.getCurrentUserId();
		List<ProjectDTO> projects = projectService.getProjectsForWeek(userId, weekId);
		return ResponseEntity.ok(projects);
	}

	@PostMapping("/admin/create")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> createProjectByAdmin(@RequestBody ProjectCreateDTO projectDto) {
		try {

			Project newProject = projectService.createProject(projectDto);
			ProjectDTO projectResponse = projectService.convertToDTO(newProject);
			return ResponseEntity.status(HttpStatus.CREATED).body(projectResponse);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error al crear el proyecto: " + e.getMessage());
		}
	}
}