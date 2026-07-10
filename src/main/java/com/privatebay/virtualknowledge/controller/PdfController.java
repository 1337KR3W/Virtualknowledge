package com.privatebay.virtualknowledge.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.privatebay.virtualknowledge.dto.TimeSheetRequestDTO;
import com.privatebay.virtualknowledge.service.TimeSheetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.privatebay.virtualknowledge.service.SecurityService;
import com.privatebay.virtualknowledge.service.PdfGeneratorService;

@RestController
@RequestMapping("/pdf")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Pdf", description = "Endpoints for PdfGeneration endpoints")
public class PdfController {

    
    private final TimeSheetService timeSheetService;
    private final SecurityService securityService;
    private final PdfGeneratorService pdfGeneratorService;

    
    public PdfController(TimeSheetService timeSheetService, SecurityService securityService,
			PdfGeneratorService pdfGeneratorService) {
		super();
		this.timeSheetService = timeSheetService;
		this.securityService = securityService;
		this.pdfGeneratorService = pdfGeneratorService;
	}


	@Operation(summary = "Download weekly timesheet in PDF format", description = "Downloads a pdf with selected week in timesheet component by clicking in download button")
	@GetMapping("/timesheet/{weekId}")
    public ResponseEntity<byte[]> downloadWeeklyTimesheetPdf(@PathVariable String weekId) {
        try {
            Long userId = securityService.getCurrentUserId();
            String user = securityService.getCurrentUser().getFirstName() + " " + securityService.getCurrentUser().getLastName(); 
            TimeSheetRequestDTO data = timeSheetService.getTimeSheetByWeek(userId, weekId);
            
            if (data == null || data.getRows() == null || data.getRows().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            byte[] pdfBytes = pdfGeneratorService.generateWeeklyReport(data, user);

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