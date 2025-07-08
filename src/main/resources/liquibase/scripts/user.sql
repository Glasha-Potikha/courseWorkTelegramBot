--liquibase formatted sql
--changeset glahaty:0createTable
CREATE TABLE reminder(
id SERIAL PRIMARY KEY,
chat_id BIGINT NOT NULL,
text VARCHAR(500) NOT NULL,
reminder_time TIMESTAMP NOT NULL
)