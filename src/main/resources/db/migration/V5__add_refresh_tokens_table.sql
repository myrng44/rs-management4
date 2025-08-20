CREATE TABLE refresh_token (
                               id VARCHAR(255) PRIMARY KEY,
                               created_at TIMESTAMP,
                               created_by BIGINT,
                               ip_address VARCHAR(255),
                               device_session VARCHAR(255),
                               expired_time TIMESTAMP NOT NULL
);
