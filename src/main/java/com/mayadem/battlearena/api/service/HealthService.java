package com.mayadem.battlearena.api.service;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import com.mayadem.battlearena.api.dto.HealthStatusDto;

@Service 
public class HealthService {

    private final DataSource dataSource;

    
    public HealthService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public HealthStatusDto getHealthStatus() {
        String appStatus = "UP";
        String dbStatus;

        try (Connection connection = dataSource.getConnection()) {
            
            if (connection.isValid(1)) {
                dbStatus = "Connected";
            } else {
                dbStatus = "Connection Invalid";
                appStatus = "DOWN";
            }
        } catch (Exception e) {
            
            dbStatus = "Disconnected: " + e.getMessage();
            appStatus = "DOWN";
        }

        return new HealthStatusDto(appStatus, dbStatus);
    }
}
