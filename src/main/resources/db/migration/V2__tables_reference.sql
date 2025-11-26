CREATE TABLE health_check_result(
    id UUID PRIMARY KEY NOT NULL,
    project_id UUID NOT NULL,
    status_code INTEGER,
    latency_ms BIGINT,
    ssl_days_remaining INTEGER,
    dns_summary TEXT,
    is_health BOOLEAN,
    checked_at TIMESTAMP WITHOUT TIME ZONE,
    raw_response TEXT,
    CONSTRAINT fk_health_check_result_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

CREATE TABLE exception_log(
    id UUID PRIMARY KEY NOT NULL,
    project_id UUID NOT NULL,
    message TEXT,
    CONSTRAINT fk_exception_log_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

CREATE TABLE incident(
    id UUID PRIMARY KEY NOT NULL,
    project_id UUID NOT NULL,
    incident_type SMALLINT,
    details VARCHAR,
    occurred_at TIMESTAMP WITHOUT TIME ZONE,
    resolved_at TIMESTAMP WITHOUT TIME ZONE,
    incident_status SMALLINT,
    CONSTRAINT fk_incident_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

CREATE TABLE log_entry(
    id UUID PRIMARY KEY NOT NULL,
    project_id UUID NOT NULL,
    log_level SMALLINT,
    message VARCHAR(100),
    ip VARCHAR(15),
    user_agent VARCHAR(100),
    duration_ms BIGINT,
    extra_info VARCHAR(100),
    log_timestamp TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_log_entry_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);