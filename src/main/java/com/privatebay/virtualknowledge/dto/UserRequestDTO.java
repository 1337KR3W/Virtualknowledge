package com.privatebay.virtualknowledge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRequestDTO {

	@NotBlank
	@Size(min = 3, max = 64, message = "First name must be between 3 and 64 characters")
	private String firstName;

	@NotBlank
	@Size(min = 3, max = 64, message = "Last name must be between 3 and 64 characters")
	private String lastName;

	@Email(message = "Email should be valid")
	@NotBlank(message = "First name is mandatory")
	@Size(max = 150)
	private String email;

	@NotBlank
	@Size(min = 3, max = 16)
	private String password;

	@NotNull
	private Long roleId;

	@NotNull
	private Long departmentId;

	@NotNull
	private String status;

	public UserRequestDTO() {
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}