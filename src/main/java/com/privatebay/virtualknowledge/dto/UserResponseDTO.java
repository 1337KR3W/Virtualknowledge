package com.privatebay.virtualknowledge.dto;

import com.privatebay.virtualknowledge.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User detail data returned from API")
public class UserResponseDTO {

	@Schema(description = "Unique user identifier", example = "12")
	private Long id;

	@Schema(description = "User first name", example = "Ana María")
	private String firstName;

	@Schema(description = "User last name", example = "Romero Muñoz")
	private String lastName;

	@Schema(description = "User email", example = "ana@privatebay.com")
	private String email;

	@Schema(description = "User status", example = "ACTIVE")
	private String status;

	@Schema(description = "User role name", example = "ROLE_USER")
	private String roleName;

	@Schema(description = "User role ID", example = "2")
	private Long roleId;
	
	@Schema(description = "User department name", example = "Development")
	private String departmentName;

	@Schema(description = "User department ID", example = "9")
	private Long departmentId;

	public UserResponseDTO() {
	}

	public UserResponseDTO(User user) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.status = user.getStatus().name();
		this.roleName = user.getRole().getName().name();
		this.roleId = user.getRole().getId();
		this.departmentName = user.getDepartment().getName();
		this.departmentId = user.getDepartment().getId();

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

}