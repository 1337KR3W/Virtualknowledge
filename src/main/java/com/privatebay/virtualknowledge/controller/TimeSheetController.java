package com.privatebay.virtualknowledge.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.privatebay.virtualknowledge.dto.TimeSheetRequestDTO;
import com.privatebay.virtualknowledge.service.TimeSheetService;

@RestController
@RequestMapping("/timesheet")
@CrossOrigin(origins = "http://localhost:4200")
public class TimeSheetController {
	@Autowired
    private TimeSheetService timeSheetService;

	@PostMapping("/save")
	public ResponseEntity<Map<String, String>> save(@RequestBody TimeSheetRequestDTO request) {
	    try {
	        timeSheetService.saveWeek(request);
	        // Devolvemos un JSON {"status": "success"}
	        return ResponseEntity.ok(Collections.singletonMap("status", "success"));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(Collections.singletonMap("error", e.getMessage()));
	    }
	}
}
