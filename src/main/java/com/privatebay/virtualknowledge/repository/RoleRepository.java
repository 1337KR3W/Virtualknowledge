package com.privatebay.virtualknowledge.repository;

import com.privatebay.virtualknowledge.entity.Role;
import com.privatebay.virtualknowledge.enums.RoleType;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(RoleType name);
}