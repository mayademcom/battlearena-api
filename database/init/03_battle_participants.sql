CREATE TABLE battle_participants (
    id BIGSERIAL PRIMARY KEY,
    battle_room_id BIGINT NOT NULL,
    warrior_id BIGINT NOT NULL,
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    final_score INTEGER,
    rank_points_before INTEGER,
    rank_points_after INTEGER,
    rank_points_change INTEGER,
    result VARCHAR(50),
    position INTEGER,
    CONSTRAINT fk_battle_room
        FOREIGN KEY(battle_room_id)
        REFERENCES battle_rooms(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_warrior
        FOREIGN KEY(warrior_id)
        REFERENCES warriors(id)
        ON DELETE CASCADE,
    UNIQUE (battle_room_id, warrior_id)
);