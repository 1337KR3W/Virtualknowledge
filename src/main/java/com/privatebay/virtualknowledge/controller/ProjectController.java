package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.dto.ProjectDTO;
import com.privatebay.virtualknowledge.service.ProjectService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/findProjectsByUserId/{id}")
    public List<ProjectDTO> getProjectsByUserId(@PathVariable("id") Long userId) {
        return projectService.findProjectsByUserId(userId);
    }
    
    @GetMapping("/user/{userId}/week/{weekId}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByWeek(
            @PathVariable Long userId, 
            @PathVariable String weekId) {
        
        List<ProjectDTO> projects = projectService.getProjectsForWeek(userId, weekId);
        return ResponseEntity.ok(projects);
    }
}
