package com.privatebay.virtualknowledge.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.privatebay.virtualknowledge.dto.ProjectResponseDTO;
import com.privatebay.virtualknowledge.entity.Project;
import com.privatebay.virtualknowledge.entity.User;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

	@Mapping(target = "departmentName", source = "project.department.name")
	@Mapping(target = "departmentId", source = "project.department.id")
	@Mapping(target = "userIds", source = "users")
	ProjectResponseDTO toResponseDTO(Project project);

	default List<Long> mapUsersToIds(Set<User> users) {
		if (users == null) {
			return new ArrayList<>();
		}

		return users.stream().map(User::getId).collect(Collectors.toList());
	}

}
