package com.privatebay.virtualknowledge.dto;

public class AuthResponseDTO {
	private String token;
	private Long id;
	private String email;
	private String name;
	private String role;
	private String departmentName;

	public AuthResponseDTO() {
	}

	public AuthResponseDTO(String token, Long id, String email, String name, String role, String departmentName) {
		this.token = token;
		this.id = id;
		this.email = email;
		this.name = name;
		this.role = role;
		this.departmentName = departmentName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}