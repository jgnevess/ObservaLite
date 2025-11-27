CREATE TABLE app_users(
    id UUID PRIMARY KEY NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    password TEXT NOT NULL,
    valid_email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    account_expires_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE user_session(
    id UUID PRIMARY KEY NOT NULL,
    user_id UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    expires_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_user_user_session FOREIGN KEY (user_id) REFERENCES app_users(id) ON DELETE CASCADE
);

ALTER TABLE projects ADD COLUMN user_id UUID NOT NULL;

ALTER TABLE projects ADD CONSTRAINT fk_projects_user FOREIGN KEY (user_id) REFERENCES app_users(id) ON DELETE CASCADE;