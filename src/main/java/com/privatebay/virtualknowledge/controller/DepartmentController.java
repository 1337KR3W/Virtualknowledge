package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.dto.DepartmentRequestDTO;
import com.privatebay.virtualknowledge.dto.DepartmentResponseDTO;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.repository.UserRepository;
import com.privatebay.virtualknowledge.service.DepartmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/departments")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Departments", description = "Endpoints for departments management")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final UserRepository userRepository;

    public DepartmentController(DepartmentService departmentService, UserRepository userRepository) {
        this.departmentService = departmentService;
        this.userRepository = userRepository;
    }

	@Operation(summary = "List all departments", description = "Only admins can list all departments")
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<DepartmentResponseDTO>> getAll() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }
	
	@GetMapping("/my-department")
	public ResponseEntity<DepartmentResponseDTO> getMyDepartment(Principal principal) {
	    String username = principal.getName();
	    User user = userRepository.findByEmail(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	    return ResponseEntity.ok(departmentService.getDepartmentByUserId(user.getId()));
	}

	@Operation(summary = "Create new department", description = "Only admins can create new departments")
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DepartmentResponseDTO> create(@RequestBody DepartmentRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.createDepartment(request));
    }

	@Operation(summary = "List department by department ID", description = "Only admins can list department by department ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartment(id));
    }

	@Operation(summary = "Update department by department ID", description = "Only admins can update departments by department ID")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DepartmentResponseDTO> update(@PathVariable Long id, @RequestBody DepartmentRequestDTO request) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, request));
    }

	@Operation(summary = "Delete department by department ID", description = "Only admins can delete a department by department ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}