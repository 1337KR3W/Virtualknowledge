//package com.privatebay.virtualknowledge.service;
//
//import com.privatebay.virtualknowledge.dto.ProjectDTO;
//import com.privatebay.virtualknowledge.entity.Project;
//import com.privatebay.virtualknowledge.repository.ProjectRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ProjectServiceTest {
//
//    @Mock
//    private ProjectRepository projectRepository;
//
//    @InjectMocks
//    private ProjectService projectService;
//
//    @Test
//    void testFindProjectsByUserId_ReturnsProjectsDTO() {
//        // Arrange
//    	System.out.println(">>> Executing testFindProjectsByUserId_ReturnsProjectsDTO");
//        Long userId = 1L;
//        Project project = new Project("Project1", "Description1", LocalDateTime.now());
//        List<Project> projects = new ArrayList<>();
//        projects.add(project);
//
//        when(projectRepository.findByUserId(userId)).thenReturn(projects);
//
//        // Act
//        List<ProjectDTO> result = projectService.findProjectsByUserId(userId);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("Project1", result.get(0).getName());
//        assertEquals("Description1", result.get(0).getDescription());
//        verify(projectRepository, times(1)).findByUserId(userId);
//    }
//
//    @Test
//    void testFindProjectsByUserId_ReturnsEmptyList() {
//        // Arrange
//    	System.out.println(">>> Executing testFindProjectsByUserId_ReturnsEmpyList");
//        Long userId = 99L; // usuario que no existe
//        when(projectRepository.findByUserId(userId)).thenReturn(new ArrayList<>());
//
//        // Act
//        List<ProjectDTO> result = projectService.findProjectsByUserId(userId);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//        verify(projectRepository, times(1)).findByUserId(userId);
//    }
//}
