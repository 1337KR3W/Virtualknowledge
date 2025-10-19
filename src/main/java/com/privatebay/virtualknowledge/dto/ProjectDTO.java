package com.privatebay.virtualknowledge.dto;

import java.time.LocalDateTime;

public class ProjectDTO {
	private Long id;
	private String name;
	private String description;
	private LocalDateTime creationDate;

	//CONSTRUCTOR USING PARAMETERS
	public ProjectDTO(Long id, String name, String description, LocalDateTime creationDate) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.creationDate = creationDate;
	}

	//GETTERS AND SETTERS
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}
}
