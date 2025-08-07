package com.mayadem.battlearena.api.dto;

import java.time.LocalDateTime;


public class ChangePasswordResponseDto {

    private String message;
    private LocalDateTime changedAt;

    public ChangePasswordResponseDto(String message, LocalDateTime changedAt) {
        this.message = message;
        this.changedAt = changedAt;
    }

    // Getters

    public String getMessage() {
        return message;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }
    
}
