package com.privatebay.virtualknowledge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.privatebay.virtualknowledge.dto.TimeSheetRequestDTO;
import com.privatebay.virtualknowledge.service.TimeSheetService;
import com.privatebay.virtualknowledge.service.SecurityService;
import com.privatebay.virtualknowledge.service.PdfGeneratorService;

@RestController
@RequestMapping("/pdf")
@CrossOrigin(origins = "http://localhost:4200")
public class PdfController {

    @Autowired
    private TimeSheetService timeSheetService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @GetMapping("/timesheet/{weekId}")
    public ResponseEntity<byte[]> downloadWeeklyTimesheetPdf(@PathVariable String weekId) {
        try {
            Long userId = securityService.getCurrentUserId();
            String username = securityService.getCurrentUser().getName(); 
            TimeSheetRequestDTO data = timeSheetService.getTimeSheetByWeek(userId, weekId);
            
            if (data == null || data.getRows() == null || data.getRows().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            byte[] pdfBytes = pdfGeneratorService.generateWeeklyReport(data, username);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "timesheet_" + weekId + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}