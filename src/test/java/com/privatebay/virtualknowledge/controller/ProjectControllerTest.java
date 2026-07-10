package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.config.SecurityConfig;
import com.privatebay.virtualknowledge.dto.ProjectRequestDTO;
import com.privatebay.virtualknowledge.dto.ProjectResponseDTO;
import com.privatebay.virtualknowledge.service.JwtService;
import com.privatebay.virtualknowledge.service.ProjectService;
import com.privatebay.virtualknowledge.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
@Import(SecurityConfig.class)
public class ProjectControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProjectService projectService;

	@MockitoBean
	private SecurityService securityService;

	@MockitoBean
	private JwtService jwtService;

	@Test
	@WithMockUser(authorities = "ROLE_USER")
	void getProjectsByUserId_ShouldReturnList_WhenUserIsAuthenticated() throws Exception {
		Long userId = 1L;
		when(securityService.getCurrentUserId()).thenReturn(userId);
		when(projectService.findProjectsByUserId(userId)).thenReturn(List.of(new ProjectResponseDTO()));

		mockMvc.perform(get("/projects/my-projects")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
	}

	@Test
	@WithMockUser(authorities = "ROLE_ADMIN")
	void createProjectByAdmin_ShouldReturnCreated_WhenValid() throws Exception {
		ProjectRequestDTO request = new ProjectRequestDTO();
		request.setName("New Project");
		request.setDepartmentId(1L);
		when(projectService.createProject(any(ProjectRequestDTO.class))).thenReturn(new ProjectResponseDTO());

		mockMvc.perform(post("/projects/admin/create").contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"New Project\", \"departmentId\":1}")).andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(authorities = "ROLE_ADMIN")
	void deleteProject_ShouldReturnNoContent_WhenExists() throws Exception {
		doNothing().when(projectService).deleteProject(1L);

		mockMvc.perform(delete("/projects/admin/delete/1")).andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(authorities = "ROLE_ADMIN")
	void getAllProjects_ShouldReturnList_WhenAdmin() throws Exception {
		when(projectService.findAllProjects()).thenReturn(List.of(new ProjectResponseDTO()));

		mockMvc.perform(get("/projects/admin/all")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
	}

	@Test
	@WithMockUser
	void getProjectById_ShouldReturnProject_WhenExists() throws Exception {
		when(projectService.getProjectById(1L)).thenReturn(new ProjectResponseDTO());

		mockMvc.perform(get("/projects/1")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(authorities = "ROLE_ADMIN")
	void updateProject_ShouldReturnUpdatedProject_WhenValid() throws Exception {
		ProjectRequestDTO request = new ProjectRequestDTO();

		when(projectService.updateProject(eq(1L), any(ProjectRequestDTO.class))).thenReturn(new ProjectResponseDTO());

		mockMvc.perform(put("/projects/admin/edit/1").contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Updated Project\"}")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(authorities = "ROLE_USER")
	void getProjectsByWeek_ShouldReturnList_WhenUserIsAuthenticated() throws Exception {
		Long userId = 1L;
		String weekId = "2026-W28";
		when(securityService.getCurrentUserId()).thenReturn(userId);
		when(projectService.getProjectsForWeek(userId, weekId)).thenReturn(List.of(new ProjectResponseDTO()));

		mockMvc.perform(get("/projects/my-projects/week/" + weekId)).andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
	}
}