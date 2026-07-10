package com.privatebay.virtualknowledge.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response after login/register successfully, by containing JWT and user details")
public class AuthResponseDTO {
	
	@Schema(description = "Token JWT para las peticiones autenticadas", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
	private String token;
	
	@Schema(description = "Unique user ID", example = "1")
	private Long id;
	
	@Schema(description = "User email", example = "juan@empresa.com")
	private String email;
	
	@Schema(description = "User full name", example = "Victoria Pérez Castillejo")
	private String name;
	
	@Schema(description = "User assigned role", example = "ROLE_USER")
	private String role;
	
	@Schema(description = "Department name of user", example = "Development")
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