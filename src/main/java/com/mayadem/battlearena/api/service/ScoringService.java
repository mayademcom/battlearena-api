package com.mayadem.battlearena.api.service;

import org.springframework.stereotype.Service;
import com.mayadem.battlearena.api.entity.enums.BattleType;
import com.mayadem.battlearena.api.entity.enums.BattleResult;


@Service
public class ScoringService {
    private static final int BASE_POINTS_RANKED = 25;
    private static final int BASE_POINTS_QUICK  = 15;
    private static final int BASE_POINTS_NONE   = 0;

    public int calculateRankPointsChange(int winnerScore, int loserScore, BattleType battleType) {
        requireNonNegative(winnerScore, "winnerScore");
        requireNonNegative(loserScore, "loserScore");
        if (battleType == null) {
            throw new IllegalArgumentException("battleType cannot be null");
        }

        int basePoints = switch (battleType) {
            case RANKED -> BASE_POINTS_RANKED;
            case QUICK  -> BASE_POINTS_QUICK;
            case PRACTICE, PRIVATE -> BASE_POINTS_NONE;
        };

        if (basePoints == 0) {
            return 0; // no rank change for PRACTICE/PRIVATE
        }

        int diff = Math.max(0, winnerScore - loserScore);
        int bonus = bonusForDiff(diff);
        return basePoints + bonus;
    }

    public BattleResult determineBattleResult(int myScore, int opponentScore) {
        requireNonNegative(myScore, "myScore");
        requireNonNegative(opponentScore, "opponentScore");

        if (myScore > opponentScore)  return BattleResult.WIN;
        if (myScore < opponentScore)  return BattleResult.LOSS;
        return BattleResult.DRAW;
    }

    private static int bonusForDiff(int diff) {
        if (diff >= 50) return 10;
        if (diff >= 20) return 5;
        if (diff >= 10) return 3;
        if (diff >= 5)  return 1;
        return 0;
    }
    private static void requireNonNegative(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " cannot be negative");
        }

    }
    
}
