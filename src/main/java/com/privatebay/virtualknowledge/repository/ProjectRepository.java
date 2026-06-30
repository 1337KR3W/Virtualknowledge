package com.privatebay.virtualknowledge.repository;

import com.privatebay.virtualknowledge.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p JOIN p.users u WHERE u.id = :userId")
    List<Project> findByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Project p JOIN p.users u WHERE u.id = :userId " + 
           "AND p.startDate <= :weekEnd " + 
           "AND (p.endDate IS NULL OR p.endDate >= :weekStart)")
    List<Project> findActiveProjectsInWeek(@Param("userId") Long userId, 
                                           @Param("weekStart") LocalDate weekStart, 
                                           @Param("weekEnd") LocalDate weekEnd);
}
