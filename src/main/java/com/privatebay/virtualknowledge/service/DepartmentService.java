package com.privatebay.virtualknowledge.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.privatebay.virtualknowledge.entity.Department;
import com.privatebay.virtualknowledge.repository.DepartmentRepository;

@Service
public class DepartmentService {

	private final DepartmentRepository departmentRepository;

	public DepartmentService(DepartmentRepository departmentRepository) {
		super();
		this.departmentRepository = departmentRepository;
	}

	public List<Department> getAllDepartments() {
		return departmentRepository.findAll();
	}

	public Department createDepartment(Department department) {
		if (departmentRepository.findByName(department.getName()).isPresent()) {
			throw new IllegalArgumentException("El departamento ya existe.");
		}
		return departmentRepository.save(department);
	}

}
