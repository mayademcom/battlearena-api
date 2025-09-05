package com.mayadem.battlearena.api.repository.projection;

import com.mayadem.battlearena.api.dto.enums.StreakType;


public record StreakProjection(
    int currentStreak,
    StreakType streakType,
    int longestWinStreak
) {}