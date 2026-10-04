-- liquibase formatted sql

-- changeset annS:1
CREATE TABLE notification_task (
    id serial,
    chat_id bigint,
    message text,
    date_time timestamp
)
