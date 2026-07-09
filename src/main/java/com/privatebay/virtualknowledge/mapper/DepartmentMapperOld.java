package com.privatebay.virtualknowledge.mapper;

import com.privatebay.virtualknowledge.dto.DepartmentResponseDTO;
import com.privatebay.virtualknowledge.entity.Department;
import com.privatebay.virtualknowledge.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepartmentMapperOld {

    public DepartmentResponseDTO toResponseDTO(Department department) {
        if (department == null) return null;

        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        
        if (department.getUsers() != null) {
            List<Long> userIds = department.getUsers().stream()
                    .map(User::getId)
                    .collect(Collectors.toList());
            dto.setUserIds(userIds);
        }
        
        return dto;
    }
}