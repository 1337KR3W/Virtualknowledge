package com.privatebay.virtualknowledge.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.privatebay.virtualknowledge.entity.Department;
import com.privatebay.virtualknowledge.service.DepartmentService;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

	private final DepartmentService departmentService;

	public DepartmentController(DepartmentService departmentService) {
		super();
		this.departmentService = departmentService;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<List<Department>> getAll() {
		return ResponseEntity.ok(departmentService.getAllDepartments());
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Department> create(@RequestBody Department department) {
		return ResponseEntity.ok(departmentService.createDepartment(department));
	}

}
