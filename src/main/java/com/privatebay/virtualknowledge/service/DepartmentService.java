package com.privatebay.virtualknowledge.service;

import com.privatebay.virtualknowledge.dto.DepartmentRequestDTO;
import com.privatebay.virtualknowledge.dto.DepartmentResponseDTO;
import com.privatebay.virtualknowledge.entity.Department;
import com.privatebay.virtualknowledge.entity.Project;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.mapper.DepartmentMapper;
import com.privatebay.virtualknowledge.repository.DepartmentRepository;
import com.privatebay.virtualknowledge.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

	private final DepartmentRepository departmentRepository;
	private final UserRepository userRepository;
	private final DepartmentMapper departmentMapper;

	public DepartmentService(DepartmentRepository departmentRepository, UserRepository userRepository,
			DepartmentMapper departmentMapper) {
		this.departmentRepository = departmentRepository;
		this.userRepository = userRepository;
		this.departmentMapper = departmentMapper;
	}

	@Transactional(readOnly = true)
	public List<DepartmentResponseDTO> getAllDepartments() {
		return departmentRepository.findAll().stream().map(departmentMapper::toResponseDTO)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public DepartmentResponseDTO getDepartment(Long id) {
		return departmentRepository.findById(id).map(departmentMapper::toResponseDTO)
				.orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));
	}

	@Transactional
	public DepartmentResponseDTO createDepartment(DepartmentRequestDTO dto) {
		List<Department> existing = departmentRepository.findByName(dto.getName());
		if (!existing.isEmpty()) {
			throw new IllegalArgumentException("Department already exists.");
		}

		if (dto.getName() == null || dto.getName().isEmpty()) {
			throw new IllegalArgumentException("Department name is required");
		}

		Department department = new Department();
		department.setName(dto.getName());
		assignUsersToDepartment(department, dto.getUserIds());

		return departmentMapper.toResponseDTO(departmentRepository.save(department));
	}

	@Transactional
	public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO dto) {
		Department dept = departmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));

		dept.setName(dto.getName());
		assignUsersToDepartment(dept, dto.getUserIds());

		return departmentMapper.toResponseDTO(departmentRepository.save(dept));
	}

	private void assignUsersToDepartment(Department department, List<Long> userIds) {
		for (User u : department.getUsers()) {
			u.setDepartment(null);
		}

		department.getUsers().clear();

		if (userIds != null && !userIds.isEmpty()) {
			List<User> newUsers = userRepository.findAllById(userIds);
			for (User u : newUsers) {
				u.setDepartment(department);
				department.getUsers().add(u);
			}
		}
	}

	@Transactional
	public void deleteDepartment(Long id) {
		Department dept = departmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));

		if (dept.getUsers() != null) {
			for (User user : dept.getUsers()) {
				user.setDepartment(null);
			}
			dept.getUsers().clear();
		}

		if (dept.getProjects() != null) {
			for (Project project : dept.getProjects()) {
				project.setDepartment(null);
			}
			dept.getProjects().clear();
		}

		departmentRepository.delete(dept);
	}
}