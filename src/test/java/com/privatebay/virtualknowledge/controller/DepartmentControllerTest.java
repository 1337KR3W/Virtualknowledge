package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.dto.DepartmentResponseDTO;
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
}