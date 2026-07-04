package com.privatebay.virtualknowledge.service;

import com.privatebay.virtualknowledge.dto.UserRequestDTO;
import com.privatebay.virtualknowledge.dto.UserResponseDTO;
import com.privatebay.virtualknowledge.entity.*;
import com.privatebay.virtualknowledge.enums.UserStatus;
import com.privatebay.virtualknowledge.mapper.UserMapper;
import com.privatebay.virtualknowledge.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final DepartmentRepository departmentRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;

	public UserService(UserRepository userRepository, RoleRepository roleRepository,
			DepartmentRepository departmentRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.departmentRepository = departmentRepository;
		this.passwordEncoder = passwordEncoder;
		this.userMapper = userMapper;
	}

	@Transactional
	public void registerUser(UserRequestDTO request) {
		if (request.getRoleId() == null) {
			throw new RuntimeException("Role is required");
		}
		Role role = roleRepository.findById(request.getRoleId())
				.orElseThrow(() -> new RuntimeException("Role not found"));

		Department department = null;
		if (request.getDepartmentId() != null && request.getDepartmentId() > 0) {
			department = departmentRepository.findById(request.getDepartmentId())
					.orElseThrow(() -> new RuntimeException("Department not found"));
		}

		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRegistrationDate(LocalDateTime.now());

		user.setStatus(request.getStatus() != null ? UserStatus.valueOf(request.getStatus().toUpperCase())
				: UserStatus.ACTIVE);

		user.setRole(role);
		user.setDepartment(department);

		userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public List<UserResponseDTO> findAll() {
		return userRepository.findAll().stream().map(userMapper::toResponseDTO).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public UserResponseDTO findById(Long id) {
		return userRepository.findById(id).map(userMapper::toResponseDTO)
				.orElseThrow(() -> new RuntimeException("User not found"));
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Transactional(readOnly = true)
	public List<UserResponseDTO> findByDepartmentId(Long departmentId) {
		return userRepository.findByDepartment_Id(departmentId).stream().map(userMapper::toResponseDTO)
				.collect(Collectors.toList());
	}

	@Transactional
	public void deleteUser(Long id) {

		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

		if (user.getProjects() != null) {
			for (Project project : user.getProjects()) {
				project.getUsers().remove(user);
			}

			user.getProjects().clear();
		}

		userRepository.delete(user);
	}

	@Transactional
	public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setEmail(dto.getEmail());

		if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
		}

		user.setStatus(dto.getStatus() != null ? UserStatus.valueOf(dto.getStatus().toUpperCase()) : user.getStatus());

		if (dto.getRoleId() != null) {
			Role role = roleRepository.findById(dto.getRoleId())
					.orElseThrow(() -> new RuntimeException("Role not found"));
			user.setRole(role);
		}

		if (dto.getDepartmentId() != null) {
			Department dept = departmentRepository.findById(dto.getDepartmentId())
					.orElseThrow(() -> new RuntimeException("Department not found"));
			user.setDepartment(dept);
		}

		return userMapper.toResponseDTO(userRepository.save(user));
	}
}