package com.privatebay.virtualknowledge.dto;

import java.util.List;

public class UserDTO {
	private Long id;
	private String email;
	private String firstName;
	private String lastName;
	private String status;
	private List<String> roles;

	public UserDTO() {
		super();
	}

	public UserDTO(Long id, String firstName,String lastName, String email, List<String> roles, String status) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
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
