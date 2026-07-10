package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.dto.AuthResponseDTO;
import com.privatebay.virtualknowledge.dto.UserRequestDTO;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.repository.UserRepository;
import com.privatebay.virtualknowledge.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Endpoints for user authentication and registration")
public class AuthController {

	private final UserRepository userRepository;
	private final JwtService jwtService;

	private final AuthenticationManager authenticationManager;

	public AuthController(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	@Operation(summary = "User login", description = "Email and password authentication during login process and return JWT")
	@PostMapping("/login")
	public AuthResponseDTO login(@RequestBody UserRequestDTO request) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

		String token = jwtService.generateToken(user.getEmail(), user.getId(), authentication.getAuthorities());
		return new AuthResponseDTO(token, user.getId(), user.getEmail(), user.getFirstName(),
				user.getRole().getName().name(), user.getDepartment().getName());
	}
}