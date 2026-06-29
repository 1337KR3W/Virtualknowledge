package com.privatebay.virtualknowledge.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.privatebay.virtualknowledge.entity.TimeSheet;

public interface TimeSheetRepository extends JpaRepository<TimeSheet, Long> {

	void deleteByUserIdAndWorkDateBetween(Long userId, LocalDate start, LocalDate end);

	@Query("SELECT t FROM TimeSheet t " + "JOIN FETCH t.project " + "WHERE t.user.id = :userId "
			+ "AND t.workDate BETWEEN :start AND :end")
	List<TimeSheet> findByUserIdAndWorkDateBetween(@Param("userId") Long userId, @Param("start") LocalDate start,
			@Param("end") LocalDate end);
}
