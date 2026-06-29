package com.privatebay.virtualknowledge.dto;

import java.util.List;

public class UserDTO {
	private Long id;
	private String email;
	private String name;
	private String status;
	private List<String> roles;

	public UserDTO() {
		super();
	}

	public UserDTO(Long id, String name, String email, List<String> roles, String status) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.roles = roles;
		this.status = status;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> role) {
		this.roles = role;
	}

}
