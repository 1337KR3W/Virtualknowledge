package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.config.JwtAuthFilter;
import com.privatebay.virtualknowledge.dto.ProjectTimeRowDTO;
import com.privatebay.virtualknowledge.dto.TimeSheetRequestDTO;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.service.PdfGeneratorService;
import com.privatebay.virtualknowledge.service.SecurityService;
import com.privatebay.virtualknowledge.service.TimeSheetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PdfController.class)
public class PdfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TimeSheetService timeSheetService;

    @MockitoBean
    private SecurityService securityService;

    @MockitoBean
    private PdfGeneratorService pdfGeneratorService;
    
    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    @WithMockUser
    void downloadWeeklyTimesheetPdf_ShouldReturnPdf_WhenDataExists() throws Exception {

    	String weekId = "2026-W28";
        TimeSheetRequestDTO data = new TimeSheetRequestDTO();
        data.setRows(new ArrayList<>());
        data.getRows().add(new ProjectTimeRowDTO());

        User mockUser = new User();
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");

        when(securityService.getCurrentUserId()).thenReturn(1L);
        when(securityService.getCurrentUser()).thenReturn(mockUser);
        when(timeSheetService.getTimeSheetByWeek(1L, weekId)).thenReturn(data);
        when(pdfGeneratorService.generateWeeklyReport(any(), anyString())).thenReturn(new byte[]{1, 2, 3});

        mockMvc.perform(get("/pdf/timesheet/" + weekId)
               .accept(MediaType.APPLICATION_PDF))
               .andExpect(status().isOk());
    }

}