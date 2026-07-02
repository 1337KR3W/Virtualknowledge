package com.privatebay.virtualknowledge.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "timesheets")
public class TimeSheet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	@Column(name = "work_date", nullable = false)
	private LocalDate workDate;

	@Column(nullable = false)
	private BigDecimal hours;

	@Column(columnDefinition = "TEXT")
	private String comment;

	@Column(name = "global_comment", columnDefinition = "TEXT")
	private String globalComment;

	@Column(name = "week_id", length = 10)
	private String weekId;

	public TimeSheet() {
	}

	public TimeSheet(Long id, User user, Project project, LocalDate workDate, BigDecimal hours, String comment) {
		super();
		this.id = id;
		this.user = user;
		this.project = project;
		this.workDate = workDate;
		this.hours = hours;
		this.comment = comment;
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public LocalDate getWorkDate() {
		return workDate;
	}

	public void setWorkDate(LocalDate workDate) {
		this.workDate = workDate;
	}

	public BigDecimal getHours() {
		return hours;
	}

	public void setHours(BigDecimal hours) {
		this.hours = hours;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getGlobalComment() {
		return globalComment;
	}

	public void setGlobalComment(String globalComment) {
		this.globalComment = globalComment;
	}

	public String getWeekId() {
		return weekId;
	}

	public void setWeekId(String weekId) {
		this.weekId = weekId;
	}
}