package com.privatebay.virtualknowledge.controller;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@RestController
@RequestMapping("/system")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "System", description="Endpoints for system endpoints")
public class SystemController {

	@Operation(summary = "Get version", description = "Return actual snapshot version")
    @GetMapping("/version")
    public Map<String, String> getVersion() {
        return Map.of(
            "version", "1.0.0-SNAPSHOT",
            "status", "UP"
        );
    }
}