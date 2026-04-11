package com.privatebay.virtualknowledge.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.privatebay.virtualknowledge.dto.TimeSheetRequestDTO;

import com.privatebay.virtualknowledge.service.TimeSheetService;
import com.privatebay.virtualknowledge.service.SecurityService;

@RestController
@RequestMapping("/timesheet")
@CrossOrigin(origins = "http://localhost:4200")
public class TimeSheetController {

    @Autowired
    private TimeSheetService timeSheetService;

    @Autowired
    private SecurityService securityService; 

    /**
     * Guarda el reporte semanal.
     * El ID del usuario se inyecta desde el Token.
     */
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> save(@RequestBody TimeSheetRequestDTO request) {
        try {
            // SEGURIDAD: Obtenemos el ID del token y lo forzamos en el request
            Long userId = securityService.getCurrentUserId();
            request.setUserId(userId); 

            timeSheetService.saveWeek(request);
            return ResponseEntity.ok(Collections.singletonMap("status", "success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * Obtiene el reporte de una semana específica para el usuario logueado.
     */
    @GetMapping("/my-timesheet/{weekId}")
    public ResponseEntity<TimeSheetRequestDTO> getTimeSheetByWeek(@PathVariable String weekId) {
        Long userId = securityService.getCurrentUserId();
        
        // Aquí llamas a tu servicio. 
        // El servicio debería devolver un TimeSheetRequestDTO relleno con los datos de la DB.
        TimeSheetRequestDTO data = timeSheetService.getTimeSheetByWeek(userId, weekId);
        
        return ResponseEntity.ok(data);
    }
}