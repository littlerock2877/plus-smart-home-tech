DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
CREATE
EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS delivery_address
(
    id      UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    country VARCHAR,
    city    VARCHAR,
    street  VARCHAR,
    house   VARCHAR,
    flat    VARCHAR
);

CREATE TABLE IF NOT EXISTS delivery
(
    delivery_id      UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    delivery_from_id UUID,
    delivery_to_id   UUID,
    delivery_weight  DOUBLE PRECISION,
    delivery_volume  DOUBLE PRECISION,
    fragile          BOOLEAN,
    order_id         UUID,
    delivery_state   VARCHAR,
    FOREIGN KEY (delivery_from_id) REFERENCES delivery_address (id),
    FOREIGN KEY (delivery_to_id) REFERENCES delivery_address (id)
);