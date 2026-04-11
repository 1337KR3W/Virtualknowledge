package com.privatebay.virtualknowledge.config;

import com.privatebay.virtualknowledge.entity.ApiKeyEntity;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.repository.ApiKeyRepository;
import com.privatebay.virtualknowledge.repository.UserRepository;
import com.privatebay.virtualknowledge.service.ApiKeyService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private final ApiKeyService apiKeyService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, ApiKeyService apiKeyService ) {
        this.apiKeyService = apiKeyService;
    	this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> body) { // Cambiado a Object
        String name = body.get("name");
        String email = body.get("email");
        String password = passwordEncoder.encode(body.get("password"));
        String role = body.get("role");
        String status = body.get("status");
        
        User user = new User(name, email, password, java.time.LocalDateTime.now(), role, status);
        User savedUser = userRepository.save(user);
        
        String token = jwtService.generateToken(email);
        
        return Map.of(
            "token", token,
            "id", savedUser.getId()
        );
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) { // Cambiado a Object
        User user = userRepository.findByEmail(body.get("email"))
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(body.get("password"), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());
        
        // AQUÍ ESTÁ LA CLAVE: Incluimos el ID en la respuesta
        return Map.of(
            "token", token,
            "id", user.getId()
        );
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
        
        // 3. Generamos el JWT para ese servicio específico
        String token = jwtService.generateToken(serviceName);

        return Map.of("token", token);
    }
}
