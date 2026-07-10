package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.dto.DepartmentResponseDTO;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.repository.UserRepository;
import com.privatebay.virtualknowledge.service.DepartmentService;
import com.privatebay.virtualknowledge.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private DepartmentService departmentService;

	@MockitoBean
	private JwtService jwtService;
	
	@MockitoBean
	private UserRepository userRepository;

	@Test
	@WithMockUser(authorities = "ROLE_ADMIN")
	void getAll_ShouldReturnList_WhenAdmin() throws Exception {
		when(departmentService.getAllDepartments()).thenReturn(List.of(new DepartmentResponseDTO()));

		mockMvc.perform(get("/departments")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
	}

	@Test
	@WithMockUser(authorities = "ROLE_ADMIN")
	void create_ShouldReturnCreated_WhenValid() throws Exception {
		mockMvc.perform(
				post("/departments").with(csrf()).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"IT\"}"))
				.andExpect(status().isCreated());
	}


	@Test
	@WithMockUser(authorities = "ROLE_ADMIN")
	void delete_ShouldReturnNoContent_WhenValid() throws Exception {
		doNothing().when(departmentService).deleteDepartment(1L);

		mockMvc.perform(delete("/departments/1").with(csrf())).andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser(username = "user@test.com")
	void getMyDepartment_ShouldReturnDepartment_WhenUserExists() throws Exception {

		String email = "user@test.com";
	    User mockUser = new User();
	    mockUser.setId(1L);
	    
	    DepartmentResponseDTO responseDTO = new DepartmentResponseDTO();
	    responseDTO.setName("IT");

	    when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
	    when(departmentService.getDepartmentByUserId(1L)).thenReturn(responseDTO);

	    mockMvc.perform(get("/departments/my-department")
	            .with(user(email)))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.name").value("IT"));
	}
}