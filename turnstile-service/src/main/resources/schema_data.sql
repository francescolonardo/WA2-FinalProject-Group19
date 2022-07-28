DROP TABLE IF EXISTS turnstile CASCADE;
DROP TABLE IF EXISTS turnstile_validation CASCADE;
CREATE TABLE IF NOT EXISTS turnstile(
    turnstile_id        BIGSERIAL,
    zid                 VARCHAR(20),
    PRIMARY KEY(turnstile_id)
);
CREATE TABLE IF NOT EXISTS turnstile_validation(
    id                  BIGSERIAL,
    turnstile_id        BIGINT,
    ticket_sub          BIGINT,
    username            VARCHAR(20),
    date_time           TIMESTAMP,
    PRIMARY KEY(id)
);
