package com.privatebay.virtualknowledge.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "projects")
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 32)
	private String name;
	
    @Column(nullable = false, length = 300, unique = true)
	private String description;
	
    @Column(name = "creatio_date", nullable = false, updatable = false)
	private LocalDateTime creationDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	//DEFAULT CONSTRUCTOR
	public Project(LocalDateTime creationDate) {
		super();
		this.creationDate = LocalDateTime.now();
	}

	//CONSTRUCTOR USING FIELDS
	public Project(String name, String description, LocalDateTime creationDate) {
		super();
		this.name = name;
		this.description = description;
		this.creationDate = creationDate;
	}

	
	//GETTERS AND SETTERS
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public User getUser() {
		return user;
	}
	
	
}
