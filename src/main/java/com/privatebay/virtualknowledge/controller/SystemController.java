package com.privatebay.virtualknowledge.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/system")
@CrossOrigin(origins = "http://localhost:4200")
public class SystemController {

    @GetMapping("/version")
    public Map<String, String> getVersion() {
        return Map.of(
            "version", "1.0.0-SNAPSHOT",
            "status", "UP"
        );
    }
}