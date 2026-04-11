package com.privatebay.virtualknowledge.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.privatebay.virtualknowledge.dto.*;
import com.privatebay.virtualknowledge.entity.*;
import com.privatebay.virtualknowledge.repository.*;

import jakarta.transaction.Transactional;

@Service
public class TimeSheetService {
    @Autowired
    private TimeSheetRepository timeSheetRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;

    /**
     * Recupera los datos de una semana y los transforma al formato del DTO
     */
    public TimeSheetRequestDTO getTimeSheetByWeek(Long userId, String weekId) {
        // 1. Calcular rango de fechas (Basado en ISO Week: Lunes a Domingo)
        LocalDate monday = LocalDate.parse(weekId + "-1", DateTimeFormatter.ISO_WEEK_DATE);
        LocalDate sunday = monday.plusDays(6);

        // 2. Obtener registros de la DB
        List<TimeSheet> entries = timeSheetRepository.findByUserIdAndWorkDateBetween(userId, monday, sunday);

        // 3. Agrupar por Proyecto para construir las filas (rows)
        Map<Long, ProjectTimeRowDTO> rowsMap = new HashMap<>();

        for (TimeSheet ts : entries) {
            Long pid = ts.getProject().getId();
            ProjectTimeRowDTO row = rowsMap.computeIfAbsent(pid, k -> {
                ProjectTimeRowDTO newRow = new ProjectTimeRowDTO();
                newRow.setPid(pid);
                newRow.setProjectName(ts.getProject().getName());
                newRow.setDays(new HashMap<>());
                return newRow;
            });

            // Mapear la fecha al Key (mon, tue, etc.)
            String dayKey = ts.getWorkDate().getDayOfWeek().name().substring(0, 3).toLowerCase();
            row.getDays().put(dayKey, new TimeEntryDTO(ts.getHours(), ts.getComment()));
        }

        // 4. Montar el DTO final
        TimeSheetRequestDTO response = new TimeSheetRequestDTO();
        response.setWeekId(weekId);
        response.setUserId(userId);
        response.setRows(new ArrayList<>(rowsMap.values()));
        
        return response;
    }

    @Transactional
    public void saveWeek(TimeSheetRequestDTO request) {
        // Tu lógica actual de parseo
        LocalDate monday = LocalDate.parse(request.getWeekId() + "-1", DateTimeFormatter.ISO_WEEK_DATE);
        LocalDate sunday = monday.plusDays(6);

        // Borrado preventivo (IDOR protegido por el controller que inyecta el userId)
        timeSheetRepository.deleteByUserIdAndWorkDateBetween(request.getUserId(), monday, sunday);

        User user = userRepository.findById(request.getUserId())
                                  .orElseThrow(() -> new RuntimeException("User not found"));
        
        for (ProjectTimeRowDTO row : request.getRows()) {
            Project project = projectRepository.findById(row.getPid())
                                               .orElseThrow(() -> new RuntimeException("Project not found"));
            
            row.getDays().forEach((dayKey, entry) -> {
                if (entry != null && entry.getHours() != null && entry.getHours().compareTo(BigDecimal.ZERO) > 0) {
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