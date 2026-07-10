package com.privatebay.virtualknowledge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.privatebay.virtualknowledge.dto.TimeSheetRequestDTO;
import com.privatebay.virtualknowledge.service.JwtService;
import com.privatebay.virtualknowledge.service.SecurityService;
import com.privatebay.virtualknowledge.service.TimeSheetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TimeSheetController.class)
public class TimeSheetControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private TimeSheetService timeSheetService;

	@MockitoBean
	private SecurityService securityService;
	
	@MockitoBean
	private JwtService jwtService;

	@Test
	@WithMockUser
	void save_ShouldReturnSuccess_WhenValidRequest() throws Exception {
		TimeSheetRequestDTO request = new TimeSheetRequestDTO();
		when(securityService.getCurrentUserId()).thenReturn(1L);
		doNothing().when(timeSheetService).saveWeek(any(TimeSheetRequestDTO.class));

		mockMvc.perform(post("/timesheet/save").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("success"));
	}

	@Test
	@WithMockUser
	void save_ShouldReturnError_WhenExceptionOccurs() throws Exception {
		TimeSheetRequestDTO request = new TimeSheetRequestDTO();
		when(securityService.getCurrentUserId()).thenReturn(1L);
		doThrow(new RuntimeException("Database error")).when(timeSheetService).saveWeek(any());

		mockMvc.perform(post("/timesheet/save").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.error").value("Database error"));
	}

	@Test
	@WithMockUser
	void getTimeSheetByWeek_ShouldReturnData_WhenValidWeekId() throws Exception {
		String weekId = "2026-W28";
		TimeSheetRequestDTO response = new TimeSheetRequestDTO();

		when(securityService.getCurrentUserId()).thenReturn(1L);
		when(timeSheetService.getTimeSheetByWeek(1L, weekId)).thenReturn(response);

		mockMvc.perform(get("/timesheet/my-timesheet/" + weekId)).andExpect(status().isOk());
	}
}