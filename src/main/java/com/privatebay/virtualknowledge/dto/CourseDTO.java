package com.privatebay.virtualknowledge.dto;

import java.time.LocalDateTime;

public class CourseDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime creationDate;

    public CourseDTO(Long id, String title, String description, LocalDateTime creationDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getCreationDate() { return creationDate; }
}
