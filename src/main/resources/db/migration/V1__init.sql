CREATE TABLE projects (
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(50) NOT NULL,
    url VARCHAR NOT NULL,
    check_interval BIGINT NOT NULL,
    project_status SMALLINT,
    api_key_hash TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    last_checked_at TIMESTAMP WITHOUT TIME ZONE
);