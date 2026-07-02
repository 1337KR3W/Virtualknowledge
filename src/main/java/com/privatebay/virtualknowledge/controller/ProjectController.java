package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.dto.ProjectRequestDTO;
import com.privatebay.virtualknowledge.dto.ProjectResponseDTO;
import com.privatebay.virtualknowledge.service.ProjectService;
import com.privatebay.virtualknowledge.service.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {

    private final ProjectService projectService;
    private final SecurityService securityService;

    public ProjectController(ProjectService projectService, SecurityService securityService) {
        this.projectService = projectService;
        this.securityService = securityService;
    }

    @GetMapping("/my-projects")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByUserId() {
        Long userId = securityService.getCurrentUserId();
        return ResponseEntity.ok(projectService.findProjectsByUserId(userId));
    }

    @GetMapping("/my-projects/week/{weekId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByWeek(@PathVariable String weekId) {
        Long userId = securityService.getCurrentUserId();
        return ResponseEntity.ok(projectService.getProjectsForWeek(userId, weekId));
    }

    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectResponseDTO> createProjectByAdmin(@RequestBody ProjectRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request));
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.findAllProjects());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PutMapping("/admin/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable Long id, @RequestBody ProjectRequestDTO request) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    @DeleteMapping("/admin/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}