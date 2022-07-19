DROP TABLE IF EXISTS transaction CASCADE;
CREATE TABLE IF NOT EXISTS transaction(
    id               BIGSERIAL,
    order_id         BIGINT,
    order_status     VARCHAR(20),
    username         VARCHAR(20),
    total_cost       FLOAT,
    PRIMARY KEY(order_id)
);
