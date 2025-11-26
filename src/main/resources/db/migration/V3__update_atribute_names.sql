ALTER TABLE log_entry RENAME COLUMN log_level TO level;

ALTER TABLE health_check_result RENAME COLUMN is_health TO is_healthy;
