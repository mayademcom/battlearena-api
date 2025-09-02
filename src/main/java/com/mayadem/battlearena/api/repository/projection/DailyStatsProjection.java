package com.mayadem.battlearena.api.repository.projection;

import java.time.LocalDate;


public interface DailyStatsProjection {
    LocalDate getBattleDate();
    Integer getBattleCount();
}
