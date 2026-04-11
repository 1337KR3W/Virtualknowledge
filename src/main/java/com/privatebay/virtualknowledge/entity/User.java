package com.privatebay.virtualknowledge.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	// ATRIBUTES
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 32)
	private String name;

	@Column(nullable = false, length = 150, unique = true)
	private String email;

	@Column(nullable = false, length = 64)
	private String password;

	@Column(name = "registration_date", nullable = false, updatable = false)
	private LocalDateTime registrationDate;

	@Column(nullable = false, length = 20)
	private String role = "USER";

	@Column(nullable = false, length = 20)
	private String status = "ACTIVE";

	// RELATIONSHIPS
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Project> projects;

	// EMPTY CONSTRUCTOR
	public User() {

	}

	// DEFAULT CONSTRUCTOR
	public User(LocalDateTime registrationDate) {
		super();
		this.registrationDate = LocalDateTime.now();
	}

	// CONSTRUCTOR USING FIELDS
	public User(String name, String email, String password, LocalDateTime registrationDate, String role,
			String status) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.registrationDate = registrationDate;
		this.role = role;
		this.status = status;
	}

	// GETTERS AND SETTERS
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Long getId() {
		return id;
	}

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
