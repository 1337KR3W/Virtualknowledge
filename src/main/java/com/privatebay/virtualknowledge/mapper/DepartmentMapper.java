package com.privatebay.virtualknowledge.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.privatebay.virtualknowledge.dto.DepartmentResponseDTO;
import com.privatebay.virtualknowledge.entity.Department;
import com.privatebay.virtualknowledge.entity.User;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

	@Mapping(target = "userIds", source = "users")
	DepartmentResponseDTO toResponseDTO(Department department);

	default List<Long> mapUsersToIds(List<User> users) {
		if (users == null) {
			return null;
		}

		return users.stream().map(User::getId).collect(Collectors.toList());
	}

}
