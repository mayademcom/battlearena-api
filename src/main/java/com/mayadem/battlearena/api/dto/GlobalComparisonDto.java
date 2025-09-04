package com.mayadem.battlearena.api.dto;

public record GlobalComparisonDto(
    double averageWinRate,
    double averageScore,
    int averageBattles
) {}