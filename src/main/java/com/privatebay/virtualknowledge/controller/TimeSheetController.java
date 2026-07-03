package com.privatebay.virtualknowledge.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.privatebay.virtualknowledge.dto.TimeSheetRequestDTO;

import com.privatebay.virtualknowledge.service.TimeSheetService;
import com.privatebay.virtualknowledge.service.SecurityService;

@RestController
@RequestMapping("/timesheet")
@CrossOrigin(origins = "http://localhost:4200")
public class TimeSheetController {

	@Autowired
	private TimeSheetService timeSheetService;

	@Autowired
	private SecurityService securityService;

	@PostMapping("/save")
	public ResponseEntity<Map<String, String>> save(@RequestBody TimeSheetRequestDTO request) {
		try {

			Long userId = securityService.getCurrentUserId();
			request.setUserId(userId);

			timeSheetService.saveWeek(request);
			return ResponseEntity.ok(Collections.singletonMap("status", "success"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", e.getMessage()));
		}
	}

	@GetMapping("/my-timesheet/{weekId}")
	public ResponseEntity<TimeSheetRequestDTO> getTimeSheetByWeek(@PathVariable String weekId) {
		Long userId = securityService.getCurrentUserId();

		TimeSheetRequestDTO data = timeSheetService.getTimeSheetByWeek(userId, weekId);

		return ResponseEntity.ok(data);
	}
}