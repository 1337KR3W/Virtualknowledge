package com.privatebay.virtualknowledge.service;

import com.privatebay.virtualknowledge.entity.Project;
import com.privatebay.virtualknowledge.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findProjectsByUserId(Long userId) {
        return projectRepository.findByUserId(userId);
    }
}
