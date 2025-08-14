package com.mayadem.battlearena.api.dto;

import java.time.LocalDateTime;

public class ChangePasswordResponseDto {

    private final String message;
    private final LocalDateTime changedAt;

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
