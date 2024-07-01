CREATE SCHEMA userdb;

CREATE TABLE userdb.users
(
    id            VARCHAR(100) PRIMARY KEY,
    first_name    VARCHAR(100)                      NOT NULL,
    second_name   VARCHAR(100)                      NOT NULL,
    birth_date    DATE                              NOT NULL,
    sex           CHAR(1) CHECK (sex IN ('M', 'F')) NOT NULL,
    biography     TEXT                              NOT NULL,
    city          VARCHAR(100)                      NOT NULL,
    password_hash VARCHAR(100)                      NOT NULL
);