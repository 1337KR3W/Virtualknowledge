package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.entity.Project;
import com.privatebay.virtualknowledge.service.ProjectService;
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
    public List<Project> getProjectsByUserId(@PathVariable("id") Long userId) {
        return projectService.findProjectsByUserId(userId);
    }
}
