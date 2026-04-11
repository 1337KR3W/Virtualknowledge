package com.privatebay.virtualknowledge.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.privatebay.virtualknowledge.dto.ProjectTimeRowDTO;
import com.privatebay.virtualknowledge.dto.TimeSheetRequestDTO;
import com.privatebay.virtualknowledge.entity.Project;
import com.privatebay.virtualknowledge.entity.TimeSheet;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.repository.ProjectRepository;
import com.privatebay.virtualknowledge.repository.TimeSheetRepository;
import com.privatebay.virtualknowledge.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class TimeSheetService {
	@Autowired
    private TimeSheetRepository timeSheetRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Transactional
    public void saveWeek(TimeSheetRequestDTO request) {
        // 1. Calcular el rango de fechas (Lunes a Domingo) basado en weekId
        // Usamos una utilidad de Java Time para "2026-W15"
        LocalDate monday = LocalDate.parse(request.getWeekId() + "-1", 
                           DateTimeFormatter.ISO_WEEK_DATE);
        LocalDate sunday = monday.plusDays(6);

        // 2. Borrar registros existentes para esa semana y usuario
        timeSheetRepository.deleteByUserIdAndWorkDateBetween(request.getUserId(), monday, sunday);

        // 3. Mapear y Guardar nuevos registros
        User user = userRepository.findById(request.getUserId()).get();
        
        for (ProjectTimeRowDTO row : request.getRows()) {
            Project project = projectRepository.findById(row.getPid()).get();
            
            row.getDays().forEach((dayKey, entry) -> {
                if (entry.getHours() != null && entry.getHours().compareTo(BigDecimal.ZERO) > 0) {
                    TimeSheet ts = new TimeSheet();
                    ts.setUser(user);
                    ts.setProject(project);
                    ts.setHours(entry.getHours());
                    ts.setComment(entry.getComment());
                    ts.setWorkDate(calculateDate(monday, dayKey));
                    timeSheetRepository.save(ts);
                }
            });
        }
    }

    private LocalDate calculateDate(LocalDate monday, String dayKey) {
        return switch (dayKey.toLowerCase()) {
            case "mon" -> monday;
            case "tue" -> monday.plusDays(1);
            case "wed" -> monday.plusDays(2);
            case "thu" -> monday.plusDays(3);
            case "fri" -> monday.plusDays(4);
            case "sat" -> monday.plusDays(5);
            case "sun" -> monday.plusDays(6);
            default -> monday;
        };
    }
}
