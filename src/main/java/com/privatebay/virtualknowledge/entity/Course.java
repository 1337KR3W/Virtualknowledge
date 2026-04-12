package com.privatebay.virtualknowledge.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 64)
	private String title;

	@Column(nullable = false, length = 300)
	private String description;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(name = "creation_date", nullable = false, updatable = false)
	private LocalDateTime creationDate;

	public Course() {
		super();
	}

	public Course(Long id, String title, String description, String content, LocalDateTime creationDate) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.content = content;
		this.creationDate = creationDate != null ? creationDate : LocalDateTime.now();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

}
