package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.dto.UserRequestDTO;
import com.privatebay.virtualknowledge.dto.UserResponseDTO;
import com.privatebay.virtualknowledge.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @PostMapping("/admin/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registerByAdmin(@RequestBody UserRequestDTO request) {
        userService.registerUser(request);
        return ResponseEntity.ok("Usuario creado exitosamente");
    }

    @GetMapping("/admin/department/{departmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getUsersByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(userService.findByDepartmentId(departmentId));
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
}