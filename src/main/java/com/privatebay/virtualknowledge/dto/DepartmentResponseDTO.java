package com.privatebay.virtualknowledge.dto;

public class DepartmentResponseDTO {
    private Long id;
    private String name;

    public DepartmentResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}