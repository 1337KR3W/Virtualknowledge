package com.privatebay.virtualknowledge.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.privatebay.virtualknowledge.entity.TimeSheet;

public interface TimeSheetRepository extends JpaRepository<TimeSheet, Long>{

    void deleteByUserIdAndWorkDateBetween(Long userId, LocalDate start, LocalDate end);    
    List<TimeSheet> findByUserIdAndWorkDateBetween(Long userId, LocalDate start, LocalDate end);
}
