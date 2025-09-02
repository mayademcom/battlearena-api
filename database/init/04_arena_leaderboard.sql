CREATE OR REPLACE VIEW arena_leaderboard AS
SELECT 
    w.id,
    RANK() OVER (ORDER BY w.rank_points DESC) AS rank_position,
    w.username,
    w.display_name,
    w.rank_points,
    w.total_battles,
    w.victories,
    w.defeats,
    CASE 
        WHEN w.total_battles > 0 THEN (w.victories::decimal / w.total_battles) * 100
        ELSE 0
    END AS win_rate
FROM warriors w;
