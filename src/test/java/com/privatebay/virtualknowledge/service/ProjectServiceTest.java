package com.privatebay.virtualknowledge.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.privatebay.virtualknowledge.dto.ProjectRequestDTO;
import com.privatebay.virtualknowledge.dto.ProjectResponseDTO;
import com.privatebay.virtualknowledge.entity.Department;
import com.privatebay.virtualknowledge.entity.Project;
import com.privatebay.virtualknowledge.exception.ConflictException;
import com.privatebay.virtualknowledge.mapper.ProjectMapper;
import com.privatebay.virtualknowledge.repository.DepartmentRepository;
import com.privatebay.virtualknowledge.repository.ProjectRepository;
import com.privatebay.virtualknowledge.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

	@Mock
	private ProjectRepository projectRepository;
	
	@Mock
	private DepartmentRepository departmentRepository;
	
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private ProjectService projectService;

	
	@Mock
	private ProjectMapper projectMapper;
	
	
	@Test
	void createProject_ShouldCreateProject_WhenNameIsUnique() {
		
		ProjectRequestDTO dto = new ProjectRequestDTO("Successfull Project");
		
		dto.setStartDate(LocalDateTime.now());
		dto.setDepartmentId(1L);
		
		Department dept = new Department("New IT Department");

		when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));
		when(projectRepository.existsByName("Successfull Project")).thenReturn(false);
		when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		ProjectResponseDTO responseDto = new ProjectResponseDTO();
	    responseDto.setName("Successfull Project");
	    when(projectMapper.toResponseDTO(any(Project.class))).thenReturn(responseDto);
	    ProjectResponseDTO result = projectService.createProject(dto);
		
	    assertNotNull(result);
	    assertEquals("Successfull Project", result.getName());
	    verify(projectRepository, times(1)).save(any(Project.class));
	    verify(departmentRepository, times(1)).findById(1L);
	}
	
	@Test
	void createProject_ShouldThrowException_WhenNameAlreadyExists() {
		
		ProjectRequestDTO dto = new ProjectRequestDTO("Proyecto Alfa");
		
		when(projectRepository.existsByName("Proyecto Alfa")).thenReturn(true);
		
		assertThrows(ConflictException.class, () -> {
	        projectService.createProject(dto);
	    });
		
		verify(projectRepository, never()).save(any(Project.class));
	    verifyNoInteractions(departmentRepository);
	}

}
