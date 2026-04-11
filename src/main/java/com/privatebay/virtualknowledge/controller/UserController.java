package com.privatebay.virtualknowledge.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.privatebay.virtualknowledge.dto.UserDTO;
import com.privatebay.virtualknowledge.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
        this.userService = userService;
    }

	@GetMapping("/profile/{id}")
	public ResponseEntity<?> getProfile(@PathVariable(value = "id", required = false) Long userId) {
	    if (userId == null) {
	        return ResponseEntity.badRequest().body("El ID de usuario no puede ser nulo");
	    }
	    return ResponseEntity.ok(userService.findById(userId));
	}
	
	

}
