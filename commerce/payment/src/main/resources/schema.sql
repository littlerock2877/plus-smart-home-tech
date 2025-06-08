DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS payment
(
    payment_id     UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id       UUID NOT NULL,
    total_price    DOUBLE PRECISION NOT NULL,
    delivery_price DOUBLE PRECISION NOT NULL,
    product_price  DOUBLE PRECISION NOT NULL,
    status         VARCHAR NOT NULL
);