package com.privatebay.virtualknowledge.entity;

import java.util.ArrayList;
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
@Table(name = "departments")
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100, unique = true)
	private String name;

	@OneToMany(mappedBy = "department", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<User> users = new ArrayList<>();

	@OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Project> projects = new ArrayList<>();

	public Department() {
	}

	public Department(String name) {
		super();
		this.name = name;
	}

	public Department(String name, List<User> users) {
		super();
		this.name = name;
		this.users = users;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Long getId() {
		return id;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

}
