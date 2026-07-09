package com.privatebay.virtualknowledge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.privatebay.virtualknowledge.dto.UserResponseDTO;
import com.privatebay.virtualknowledge.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	@Mapping(target = "status", expression = "java(user.getStatus() != null ? user.getStatus().name() : null)")
	@Mapping(target = "roleName", expression = "java(user.getRole() != null ? user.getRole().getName().name() : null)")
    @Mapping(target = "roleId", source = "user.role.id")
	@Mapping(target = "departmentName", source = "user.department.name")
    @Mapping(target = "departmentId", source = "user.department.id")
	
	UserResponseDTO toResponseDTO(User user);

}
