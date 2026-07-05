package com.privatebay.virtualknowledge.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.privatebay.virtualknowledge.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

	List<Department> findByName(String name);
	
	boolean existsByName(String name);
	
	

}
