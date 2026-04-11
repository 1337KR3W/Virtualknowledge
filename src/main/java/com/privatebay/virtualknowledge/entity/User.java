package com.privatebay.virtualknowledge.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	    name = "user_roles",
	    joinColumns = @JoinColumn(name = "user_id"),
	    inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles = new HashSet<>();

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
	public User(String name, String email, String password, LocalDateTime registrationDate, Set<Role> roles,
			String status) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.registrationDate = registrationDate;
		this.roles = roles;
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
	public void setRegistrationDate(LocalDateTime registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Set<Role> getRole() {
		return roles;
	}

	public void setRole(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role) {
        this.roles.add(role);
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
