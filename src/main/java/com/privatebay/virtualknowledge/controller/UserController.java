package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.entity.Role;
import com.privatebay.virtualknowledge.enums.RoleType;
import com.privatebay.virtualknowledge.repository.RoleRepository;
import com.privatebay.virtualknowledge.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable Long userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().body("El ID de usuario no puede ser nulo");
        }
        return ResponseEntity.ok(userService.findById(userId));
    }

    @PostMapping("/admin/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerByAdmin(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            
            
            if (userService.existsByEmail(email)) {
                return ResponseEntity.badRequest().body("Error: El email ya está registrado.");
            }

            User newUser = new User();
            newUser.setName(body.get("name"));
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(body.get("password")));
            newUser.setRegistrationDate(LocalDateTime.now());
            newUser.setStatus("ACTIVE");

            String roleStr = body.getOrDefault("role", "ROLE_USER");
            Role userRole = roleRepository.findByName(RoleType.valueOf(roleStr))
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
            newUser.addRole(userRole);

            userService.save(newUser);

            return ResponseEntity.ok(Map.of("message", "Usuario creado exitosamente por el administrador"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al crear usuario: " + e.getMessage());
        }
    }
}