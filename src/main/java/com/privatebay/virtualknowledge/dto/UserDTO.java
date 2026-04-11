package com.privatebay.virtualknowledge.dto;

public class UserDTO {
	private Long id;
    private String email;
    private String name;
    private String status;
    private String role;
    
    
    
    public UserDTO() {
		super();
	}

	public UserDTO(Long id, String name, String email, String role, String status) {
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}


}


