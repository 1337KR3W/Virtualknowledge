package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.dto.AuthResponseDTO;
import com.privatebay.virtualknowledge.dto.UserRequestDTO;
import com.privatebay.virtualknowledge.entity.Department;
import com.privatebay.virtualknowledge.entity.Role;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.enums.UserStatus;
import com.privatebay.virtualknowledge.repository.DepartmentRepository;
import com.privatebay.virtualknowledge.repository.RoleRepository;
import com.privatebay.virtualknowledge.repository.UserRepository;
import com.privatebay.virtualknowledge.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Collections;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final RoleRepository roleRepository;
	private final DepartmentRepository departmentRepository;
	private final AuthenticationManager authenticationManager;

	public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
			RoleRepository roleRepository, DepartmentRepository departmentRepository,
			AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.roleRepository = roleRepository;
		this.departmentRepository = departmentRepository;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/register")
	public AuthResponseDTO register(@RequestBody UserRequestDTO request) {
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
		}

		Role role = roleRepository.findById(request.getRoleId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol not found"));

		Department department = departmentRepository.findById(request.getDepartmentId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));

		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setStatus(UserStatus.valueOf(request.getStatus().toUpperCase()));
		user.setRole(role);
		user.setDepartment(department);

		User savedUser = userRepository.save(user);

		String token = jwtService.generateToken(savedUser.getEmail(), savedUser.getId(), Collections.singletonList(
				new SimpleGrantedAuthority(role.getName().name())));

		return new AuthResponseDTO(token, savedUser.getId(), savedUser.getEmail(), savedUser.getFirstName(),
				savedUser.getRole().getName().name(), savedUser.getDepartment().getName());
	}

	@PostMapping("/login")
	public AuthResponseDTO login(@RequestBody UserRequestDTO request) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

		String token = jwtService.generateToken(user.getEmail(), user.getId(), authentication.getAuthorities());
	    String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getId());
		
		return new AuthResponseDTO(token, user.getId(), user.getEmail(), user.getFirstName(),
				user.getRole().getName().name(), user.getDepartment().getName(), refreshToken);
	}
	
	@PostMapping("/refresh")
	public AuthResponseDTO refresh(@RequestHeader("Authorization") String refreshTokenHeader) {
	    if (refreshTokenHeader == null || !refreshTokenHeader.startsWith("Bearer ")) {
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
	    }
	    String refreshToken = refreshTokenHeader.substring(7);
	    String email = jwtService.extractEmail(refreshToken);
	    User user = userRepository.findByEmail(email).orElseThrow();
	    
	    String newAccessToken = jwtService.generateToken(user.getEmail(), user.getId(), 
	            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().name())));
	           
	    return new AuthResponseDTO(newAccessToken, user.getId(), user.getEmail(), 
	                               user.getFirstName(), user.getRole().getName().name(), 
	                               user.getDepartment().getName(), refreshToken);
	}
}