package com.privatebay.virtualknowledge.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

	@GetMapping("/profile")
	public Map<String, Object> getProfile() {
		return Map.of(
				"id", 1L,
				"email", "pepito@gmail.com", 
				"name", "Pepito Pérez", 
				"status", "Active", 
				"role", "USER"
				);
	}

}
