package com.privatebay.virtualknowledge.config;

import com.privatebay.virtualknowledge.entity.ApiKeyEntity;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.repository.ApiKeyRepository;
import com.privatebay.virtualknowledge.repository.UserRepository;
import com.privatebay.virtualknowledge.service.ApiKeyService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public Map<String, String> register(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String email = body.get("email");
        String password = passwordEncoder.encode(body.get("password"));
        User user = new User(name, email, password, java.time.LocalDateTime.now());
        userRepository.save(user);
        String token = jwtService.generateToken(email);
        return Map.of("token", token);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        User user = userRepository.findByEmail(body.get("email"))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🏆 Lógica de validación correcta
        String rawPassword = body.get("password"); // Contraseña en texto plano de Postman (e.g., "123")
        String encodedPassword = user.getPassword(); // Hash guardado en DB (e.g., "$2a$10$...")

        if (!passwordEncoder.matches(rawPassword, encodedPassword)) { // Compara (texto_plano, hash_DB)
            throw new RuntimeException("Invalid password"); // <-- Lanza este error si falla
        }

        String token = jwtService.generateToken(user.getEmail());
        return Map.of("token", token);
    }
    
    @PostMapping("/ssoToken")
    public Map<String, String> ssoToken(@RequestBody Map<String, String> body) {
        
        String apiKeyReceived = body.get("apiKey");
        String apiSecretRawReceived = body.get("apiSecret");

        // Usamos el servicio externo para validar
        if (!apiKeyService.validateCredentials(apiKeyReceived, apiSecretRawReceived)) {
             throw new RuntimeException("Invalid credentials for SSO");
        }

        // Generar el JWT con la identidad fija del servicio
        String serviceName = apiKeyService.getFixedServiceName();
        String token = jwtService.generateToken(serviceName);

        return Map.of("token", token);
    }
}
