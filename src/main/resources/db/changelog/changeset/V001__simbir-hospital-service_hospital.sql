-- Create `hospital` table
CREATE TABLE IF NOT EXISTS hospital
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    address       VARCHAR(255) NOT NULL,
    contact_phone VARCHAR(50) NOT NULL
);

-- Create `hospital_rooms` table
CREATE TABLE IF NOT EXISTS hospital_rooms
(
    hospital_id BIGINT NOT NULL,
    room        VARCHAR(255) NOT NULL,
    CONSTRAINT fk_hospital FOREIGN KEY (hospital_id) REFERENCES hospital (id) ON DELETE CASCADE,
    PRIMARY KEY (hospital_id, room)
);