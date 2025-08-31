CREATE TABLE battle_rooms (
    id BIGSERIAL PRIMARY KEY,
    room_code VARCHAR(10) UNIQUE NOT NULL,
    battle_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    max_participants INTEGER NOT NULL DEFAULT 2,
    current_participants INTEGER NOT NULL DEFAULT 0,
    created_by_warrior_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_created_by_warrior
        FOREIGN KEY(created_by_warrior_id)
        REFERENCES warriors(id)
        ON DELETE SET NULL
);