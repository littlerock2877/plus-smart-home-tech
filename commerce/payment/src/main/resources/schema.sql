DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
CREATE
EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS payment
(
    payment_id     UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id       UUID,
    total_price    DOUBLE PRECISION,
    delivery_price DOUBLE PRECISION,
    product_price  DOUBLE PRECISION,
    status         VARCHAR
);