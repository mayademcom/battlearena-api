package com.mayadem.battlearena.api.repository.projection;

public record RecentPerformanceProjection(
    long battles,
    long victories,
    Long rankPointsChange
) {}