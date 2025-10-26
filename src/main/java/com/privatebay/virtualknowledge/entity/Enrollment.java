package com.privatebay.virtualknowledge.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "enrollment")
public class Enrollment {
	
	
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	@JoinColumn(name="course_id")
	private Course course;
	private LocalDateTime enrolledAt;
	private Integer progress;
	
	
	public Enrollment() {
		super();
	}


	public Enrollment(Long id, User user, Course course, LocalDateTime enrolledAt, Integer progress) {
		super();
		this.id = id;
		this.user = user;
		this.course = course;
		this.enrolledAt = enrolledAt;
		this.progress = progress;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Course getCourse() {
		return course;
	}


	public void setCourse(Course course) {
		this.course = course;
	}


	public Integer getProgress() {
		return progress;
	}


	public void setProgress(Integer progress) {
		this.progress = progress;
	}


	public Long getId() {
		return id;
	}


	public LocalDateTime getEnrolledAt() {
		return enrolledAt;
	}
	
	
	

}
