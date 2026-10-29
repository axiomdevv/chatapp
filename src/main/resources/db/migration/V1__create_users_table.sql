CREATE TABLE users
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username       VARCHAR(50) UNIQUE  NOT NULL,
    email           VARCHAR(100) UNIQUE NOT NULL,
    password_hashed VARCHAR(200)        NOT NULL,
    created_at      TIMESTAMPTZ  DEFAULT now() NOT NULL
);