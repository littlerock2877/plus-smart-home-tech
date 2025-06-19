CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS "order"
(
    order_id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    payment_id       UUID NOT NULL,
    shopping_cart_id UUID NOT NULL,
    delivery_id      UUID NOT NULL,
    state            VARCHAR NOT NULL DEFAULT 'CREATED',
    delivery_weight  DOUBLE PRECISION NOT NULL,
    delivery_volume  DOUBLE PRECISION NOT NULL,
    fragile          BOOLEAN NOT NULL DEFAULT FALSE,
    total_price      DOUBLE PRECISION NOT NULL,
    delivery_price   DOUBLE PRECISION NOT NULL,
    product_price    DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS order_address
(
    order_id uuid PRIMARY KEY,
    country  VARCHAR NOT NULL,
    city     VARCHAR NOT NULL,
    street   VARCHAR NOT NULL,
    house    VARCHAR NOT NULL,
    flat     VARCHAR,
    CONSTRAINT fk_primary_key FOREIGN KEY (order_id) REFERENCES "order" (order_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS order_product
(
    product_id uuid PRIMARY KEY,
    count      BIGINT NOT NULL CHECK (count > 0),
    order_id   UUID NOT NULL,
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES "order" (order_id) ON DELETE CASCADE
);