DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS delivery_address
(
    id      UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    country VARCHAR NOT NULL,
    city    VARCHAR NOT NULL,
    street  VARCHAR NOT NULL,
    house   VARCHAR NOT NULL,
    flat    VARCHAR
);

CREATE TABLE IF NOT EXISTS delivery
(
    delivery_id      UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    delivery_from_id UUID NOT NULL,
    delivery_to_id   UUID NOT NULL,
    delivery_weight  DOUBLE PRECISION NOT NULL,
    delivery_volume  DOUBLE PRECISION NOT NULL,
    fragile          BOOLEAN NOT NULL DEFAULT FALSE,
    order_id         UUID NOT NULL,
    delivery_state   VARCHAR NOT NULL,
    FOREIGN KEY (delivery_from_id) REFERENCES delivery_address (id),
    FOREIGN KEY (delivery_to_id) REFERENCES delivery_address (id)
);