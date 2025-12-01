ALTER TABLE app_users DROP COLUMN is_active;

ALTER TABLE app_users ADD COLUMN is_active BOOLEAN NOT NULL;