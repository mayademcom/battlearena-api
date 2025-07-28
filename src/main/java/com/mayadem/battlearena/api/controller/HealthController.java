package com.mayadem.battlearena.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mayadem.battlearena.api.dto.HealthStatusDto;
import com.mayadem.battlearena.api.service.HealthService;

@RestController
@RequestMapping("/api") 
public class HealthController {

    private final HealthService healthService;

    
    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping("/health")
    public HealthStatusDto healthCheck() {
        
        return healthService.getHealthStatus();
    }
}