package com.privatebay.virtualknowledge.dto;

import java.util.Set;

public class AuthResponseDTO {
	private String token;
	private Long id;
	private String email;
	private String name;
	private Set<String> roles;

	public AuthResponseDTO(String token, Long id, String email, String name, Set<String> roles) {
		this.token = token;
		this.id = id;
		this.email = email;
		this.name = name;
		this.roles = roles;
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

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

}