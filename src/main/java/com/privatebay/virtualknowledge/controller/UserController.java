package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.dto.UserRequestDTO;
import com.privatebay.virtualknowledge.dto.UserResponseDTO;
import com.privatebay.virtualknowledge.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Users", description= "Endpoints for users management")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Obtain user profile", description = "Returns user details providing an ID")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = " EXAMPLE User not found"),
    		@ApiResponse(responseCode = "404", description = "EXAMPLE User not found")
    		
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    
    @Operation(summary = "Register new user", description = "Only admins can register new users")
    @PostMapping("/admin/register")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> registerByAdmin(@Valid @RequestBody UserRequestDTO request) {
        userService.registerUser(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "New user created successfully!");
        return ResponseEntity.ok(response);
    }

    @Operation(summary =  "List users by department ID", description = "Only admins can list users by department")
    @GetMapping("/admin/department/{departmentId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getUsersByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(userService.findByDepartmentId(departmentId));
    }

    @Operation(summary = "List all users", description = "Only admins can list all users")
    @GetMapping("/admin/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
    
    @Operation(summary = "Update user by user ID", description = "Only admins can update users")
	@PutMapping("/admin/edit/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
			@RequestBody UserRequestDTO request) {
		return ResponseEntity.ok(userService.updateUser(id, request));
	}
    
    @Operation(summary = "Delete user by user ID", description = "Only admins can delete users")
	@DeleteMapping("/admin/delete/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
}