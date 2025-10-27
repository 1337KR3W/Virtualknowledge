package com.privatebay.virtualknowledge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.privatebay.virtualknowledge.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long>{

	
	
	//List<Course> findByUserId(Long userId);
	
	
}
