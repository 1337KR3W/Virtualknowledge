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

    /**
     * USADO POR: ProjectsPage (Listado Histórico)
     * Retorna TODOS los proyectos del usuario sin filtrar por fecha.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProjectDTO>> getAllProjects(@PathVariable Long userId) {
        List<ProjectDTO> projects = projectService.findProjectsByUserId(userId);
        return ResponseEntity.ok(projects);
    }

    /**
     * USADO POR: TimeSheetComponent (Reporte Semanal)
     * Retorna solo los proyectos vigentes en la semana indicada.
     */
    @GetMapping("/user/{userId}/week/{weekId}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByWeek(
            @PathVariable Long userId, 
            @PathVariable String weekId) {
        
        List<ProjectDTO> projects = projectService.getProjectsForWeek(userId, weekId);
        return ResponseEntity.ok(projects);
    }
}
