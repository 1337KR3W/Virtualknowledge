package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.dto.ProjectDTO;
import com.privatebay.virtualknowledge.service.ProjectService;
import com.privatebay.virtualknowledge.service.SecurityService;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        
        Long userId = securityService.getCurrentUserId();
        
        List<ProjectDTO> projects = projectService.findProjectsByUserId(userId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/my-projects/week/{weekId}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByWeek(@PathVariable String weekId) {
       
        Long userId = securityService.getCurrentUserId();
        
        List<ProjectDTO> projects = projectService.getProjectsForWeek(userId, weekId);
        return ResponseEntity.ok(projects);
    }
}