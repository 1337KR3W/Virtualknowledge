package com.privatebay.virtualknowledge.repository;

import com.privatebay.virtualknowledge.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

	@Query("SELECT DISTINCT p FROM Project p JOIN p.users u WHERE u.id = :userId")
	List<Project> findProjectByUserId(@Param("userId") Long userId);

	@Query("SELECT DISTINCT p FROM Project p JOIN p.users u WHERE u.id = :userId "
			+ "AND FUNCTION('DATE', p.startDate) <= :weekEnd "
			+ "AND (p.endDate IS NULL OR FUNCTION('DATE', p.endDate) >= :weekStart)")
	List<Project> findActiveProjectsInWeek(@Param("userId") Long userId, @Param("weekStart") LocalDate weekStart,
			@Param("weekEnd") LocalDate weekEnd);

	@Query("SELECT p FROM Project p LEFT JOIN FETCH p.users WHERE p.id = :id")
	Optional<Project> findByIdWithUsers(@Param("id") Long id);

	boolean existsByName(String name);
}
