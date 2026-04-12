package com.privatebay.virtualknowledge.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.privatebay.virtualknowledge.entity.Course;
import com.privatebay.virtualknowledge.repository.CourseRepository;

@Service
public class CourseService {

	private final CourseRepository courseRepository;

	public CourseService(CourseRepository courseRepository) {
		super();
		this.courseRepository = courseRepository;
	}

	public List<Course> getAllCourses() {
		return courseRepository.findAll();
	}
}
