package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.entity.Role;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.enums.RoleType;
import com.privatebay.virtualknowledge.repository.RoleRepository;
import com.privatebay.virtualknowledge.repository.UserRepository;
import com.privatebay.virtualknowledge.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, 
                          JwtService jwtService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya está registrado");
        }

        String roleStr = body.getOrDefault("role", "ROLE_USER").toUpperCase();
        RoleType roleType = RoleType.valueOf(roleStr);
        
        Role userRole = roleRepository.findByName(roleType)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));

        User user = new User();
        user.setName(body.get("name"));
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(body.get("password")));
        user.setRegistrationDate(LocalDateTime.now());
        user.setStatus("ACTIVE");
        user.addRole(userRole);

        User savedUser = userRepository.save(user);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleType.name()));
        String token = jwtService.generateToken(email, savedUser.getId(), authorities);

        return Map.of(
            "token", token, 
            "id", savedUser.getId(), 
            "message", "Usuario registrado correctamente"
        );
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        User user = userRepository.findByEmail(body.get("email"))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

        if (!passwordEncoder.matches(body.get("password"), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña inválida");
        }

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        String token = jwtService.generateToken(user.getEmail(), user.getId(), authorities);

        // Cambiamos Map.of por un HashMap tradicional para evitar que explote si hay nulos
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("id", user.getId());
        response.put("name", user.getName() != null ? user.getName() : "");
        response.put("email", user.getEmail());
        response.put("roles", authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList()));
        response.put("status", user.getStatus() != null ? user.getStatus() : "ACTIVE");

        return response;
    }

}