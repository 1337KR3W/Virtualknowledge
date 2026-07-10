package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.dto.ProjectRequestDTO;
import com.privatebay.virtualknowledge.dto.ProjectResponseDTO;
import com.privatebay.virtualknowledge.service.ProjectService;
import com.privatebay.virtualknowledge.service.SecurityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Projects", description = "Endpoints for projects management")
public class ProjectController {

	private final ProjectService projectService;
	private final SecurityService securityService;

	public ProjectController(ProjectService projectService, SecurityService securityService) {
		this.projectService = projectService;
		this.securityService = securityService;
	}

	@Operation(summary = "List projects by user ID", description = "Return all projects from an user")
	@GetMapping("/my-projects")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<List<ProjectResponseDTO>> getProjectsByUserId() {
		Long userId = securityService.getCurrentUserId();
		return ResponseEntity.ok(projectService.findProjectsByUserId(userId));
	}

	@Operation(summary = "List projects by week ID", description = "Return all projects by week ID")
	@GetMapping("/my-projects/week/{weekId}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<List<ProjectResponseDTO>> getProjectsByWeek(@PathVariable String weekId) {
		Long userId = securityService.getCurrentUserId();
		return ResponseEntity.ok(projectService.getProjectsForWeek(userId, weekId));
	}

	@Operation(summary = "Create new project", description = "Only andmins can create new projects")
	@PostMapping("/admin/create")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ProjectResponseDTO> createProjectByAdmin(@RequestBody ProjectRequestDTO request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request));
	}

	@Operation(summary = "List all projects by project ID", description = "Only admins can list all projects ")
	@GetMapping("/admin/all")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
		return ResponseEntity.ok(projectService.findAllProjects());
	}

	@Operation(summary = "Get project by project ID", description = "Obtain a project by project ID")
	@GetMapping("/{id}")
	public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
		return ResponseEntity.ok(projectService.getProjectById(id));
	}

	@Operation(summary = "Update project by project ID", description = "Only admins can update projects")
	@PutMapping("/admin/edit/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable Long id,
			@RequestBody ProjectRequestDTO request) {
		return ResponseEntity.ok(projectService.updateProject(id, request));
	}

	@Operation(summary = "Delete project by project ID", description = "Only admins can delete projects")
	@DeleteMapping("/admin/delete/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
		projectService.deleteProject(id);
		return ResponseEntity.noContent().build();
	}
}