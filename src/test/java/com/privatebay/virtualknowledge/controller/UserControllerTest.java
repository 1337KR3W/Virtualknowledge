package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.config.SecurityConfig;
import com.privatebay.virtualknowledge.dto.UserResponseDTO;
import com.privatebay.virtualknowledge.service.JwtService;
import com.privatebay.virtualknowledge.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	@MockitoBean
	private JwtService jwtService;

	@Test
	@WithMockUser
	void getProfile_ShouldReturnUser_WhenUserExists() throws Exception {
		UserResponseDTO mockResponse = new UserResponseDTO();
		mockResponse.setId(1L);

		when(userService.findById(1L)).thenReturn(mockResponse);

		mockMvc.perform(get("/users/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
	}

	@Test
	@WithMockUser(authorities = "ROLE_ADMIN")
	void registerByAdmin_ShouldReturnOk_WhenUserIsValid() throws Exception {
		String jsonRequest = "{" + "\"firstName\":\"Tester\", " + "\"lastName\":\"TesterLast\", "
				+ "\"email\":\"juan@test.com\", " + "\"password\":\"123456\", " + "\"roleId\":1, "
				+ "\"departmentId\": 1, " + "\"status\": \"ACTIVE\"" + "}";

		mockMvc.perform(post("/users/admin/register").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andExpect(status().isOk()).andExpect(jsonPath("$.message").value("New user created successfully!"));

		verify(userService).registerUser(any());
	}

	@Test
	void registerByAdmin_ShouldReturn403_WhenNotAdmin() throws Exception {
		
		String jsonRequest = "{\"firstName\":\"Tester\", \"lastName\":\"TesterLast\", \"email\":\"juan@test.com\", \"password\":\"123456\", \"roleId\":1, \"departmentId\":1, \"status\":\"ACTIVE\"}";

		mockMvc.perform(post("/users/admin/register")
		           .with(user("user").authorities(() -> "ROLE_USER"))
		           .contentType(MediaType.APPLICATION_JSON)
		           .content(jsonRequest))
		           .andExpect(status().isForbidden());
	}
}