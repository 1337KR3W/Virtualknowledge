package com.privatebay.virtualknowledge.config;

import com.privatebay.virtualknowledge.entity.Role;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.enums.RoleType;
import com.privatebay.virtualknowledge.repository.RoleRepository;
import com.privatebay.virtualknowledge.repository.UserRepository;
import com.privatebay.virtualknowledge.service.ApiKeyService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET,
		RequestMethod.OPTIONS })
@RestController
@RequestMapping("/auth")
public class AuthController {

	private final ApiKeyService apiKeyService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final RoleRepository roleRepository;

	public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
			ApiKeyService apiKeyService, RoleRepository roleRepository) {
		this.apiKeyService = apiKeyService;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.roleRepository = roleRepository;
	}

	@PostMapping("/register")
	public Map<String, Object> register(@RequestBody Map<String, String> body) {
		String name = body.get("name");
		String email = body.get("email");
		String password = passwordEncoder.encode(body.get("password"));

		String roleStr = body.getOrDefault("role", "ROLE_USER");
		RoleType roleType = RoleType.valueOf(roleStr);
		Role userRole = roleRepository.findByName(roleType)
				.orElseThrow(() -> new RuntimeException("Error: Role not found in database."));

		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setRegistrationDate(java.time.LocalDateTime.now());
		user.setStatus("ACTIVE");
		user.addRole(userRole);

		User savedUser = userRepository.save(user);
		String token = jwtService.generateToken(email, savedUser.getId());

		return Map.of("token", token, "id", savedUser.getId(), "message", "User registered successfully");
	}

	@PostMapping("/login")
	public Map<String, Object> login(@RequestBody Map<String, String> body) {
		User user = userRepository.findByEmail(body.get("email"))
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (!passwordEncoder.matches(body.get("password"), user.getPassword())) {
			throw new RuntimeException("Invalid password");
		}

		String token = jwtService.generateToken(user.getEmail(), user.getId());

		List<String> roles = user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toList());

		return Map.of("token", token, "id", user.getId(), "name", user.getName(), "email", user.getEmail(), "roles",
				roles, "status", user.getStatus());
	}

	@PostMapping("/ssoToken")
	public Map<String, String> ssoToken(@RequestBody Map<String, String> body) {
		String apiKeyReceived = body.get("apiKey");
		String apiSecretRawReceived = body.get("apiSecret");

		if (!apiKeyService.validateCredentials(apiKeyReceived, apiSecretRawReceived)) {
			throw new org.springframework.web.server.ResponseStatusException(
					org.springframework.http.HttpStatus.UNAUTHORIZED, "Credenciales de API inválidas");
		}

		String serviceName = apiKeyService.getServiceNameByApiKey(apiKeyReceived);
		String token = jwtService.generateToken(serviceName, 0L);

		return Map.of("token", token);
	}
}