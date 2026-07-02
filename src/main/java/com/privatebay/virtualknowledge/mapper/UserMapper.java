package com.privatebay.virtualknowledge.mapper;

import com.privatebay.virtualknowledge.dto.UserResponseDTO;
import com.privatebay.virtualknowledge.entity.User;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponseDTO toResponseDTO(User user) {
        if (user == null) return null;

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setStatus(user.getStatus().name());
        
        if (user.getRole() != null) {
            dto.setRoleName(user.getRole().getName().name());
        }
        
        if (user.getDepartment() != null) {
            dto.setDepartmentName(user.getDepartment().getName());
        }
        
        return dto;
    }
}