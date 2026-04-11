package com.privatebay.virtualknowledge.dto;

import java.util.Set;

import com.privatebay.virtualknowledge.entity.Role;

public class UserDTO {
	private Long id;
    private String email;
    private String name;
    private String status;
    private Set<Role> role;
    
    
    
    public UserDTO() {
		super();
	}

	public UserDTO(Long id, String name, String email, Set<Role> role, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
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
	public Set<Role> getRole() {
		return role;
	}
	public void setRole(Set<Role> role) {
		this.role = role;
	}


}


