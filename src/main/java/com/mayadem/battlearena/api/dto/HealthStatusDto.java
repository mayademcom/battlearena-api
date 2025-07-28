package com.mayadem.battlearena.api.dto;

import java.time.Instant;


public class HealthStatusDto {

    private String status;
    private String application;
    private String version;
    private String database;
    private Instant timestamp;

    public HealthStatusDto(String status, String database) {
        this.status = status;
        this.database = database;
        this.application = "BattleArena API"; 
        this.version = "1.0.0";         
        this.timestamp = Instant.now();  
    }

  
    public String getStatus() { 
        return status; 
    }
    public String getApplication() { 
        return application; 
    }
    public String getVersion() { 
        return version; 
    }
    public String getDatabase() { 
        return database; 
    }
    public Instant getTimestamp() { 
        return timestamp; 
    }
}
