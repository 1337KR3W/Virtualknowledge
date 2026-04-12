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
import java.util.List;
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
    @PreAuthorize("hasRole('ADMIN')") // Usamos Authority para evitar el lío del prefijo ROLE_
    public ResponseEntity<?> registerByAdmin(@RequestBody Map<String, Object> body) {
        try {
            String email = (String) body.get("email");
            String password = (String) body.get("password"); // Extraemos la password del JSON
            String name = (String) body.get("name");
            
            // 1. Validación de existencia
            if (userService.existsByEmail(email)) {
                return ResponseEntity.badRequest().body("Error: El email ya está registrado.");
            }

            // 2. Mapeo manual a la Entidad (El estándar es usar un Mapper, pero aquí lo hacemos claro)
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(password)); // Encriptación vital
            newUser.setRegistrationDate(LocalDateTime.now());
            newUser.setStatus("ACTIVE");

            // 3. Manejo de Roles
            // Angular enviará un array ["ADMIN"] o ["USER"]
            Object rolesObj = body.get("roles");
            String roleStr = "USER"; // Default
            
            if (rolesObj instanceof List && !((List<?>) rolesObj).isEmpty()) {
                roleStr = ((List<?>) rolesObj).get(0).toString();
            }

            Role userRole = roleRepository.findByName(RoleType.valueOf(roleStr))
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
            newUser.addRole(userRole);

            userService.save(newUser);

            return ResponseEntity.ok(Map.of("message", "Usuario creado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}