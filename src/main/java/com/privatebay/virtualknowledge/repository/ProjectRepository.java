package com.privatebay.virtualknowledge.repository;

import com.privatebay.virtualknowledge.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUserId(Long userId);
    
    @Query("SELECT p FROM Project p WHERE p.user.id = :userId " +
            "AND p.startDate <= :weekEnd " +
            "AND (p.endDate IS NULL OR p.endDate >= :weekStart)")
     List<Project> findActiveProjectsInWeek(
         @Param("userId") Long userId, 
         @Param("weekStart") LocalDate weekStart, 
         @Param("weekEnd") LocalDate weekEnd
     );
}
