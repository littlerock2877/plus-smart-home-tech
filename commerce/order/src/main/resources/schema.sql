CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS "order"
(
    order_id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    payment_id       UUID,
    shopping_cart_id UUID,
    delivery_id      UUID,
    state            VARCHAR,
    delivery_weight  DOUBLE PRECISION,
    delivery_volume  DOUBLE PRECISION,
    fragile          BOOLEAN,
    total_price      DOUBLE PRECISION,
    delivery_price   DOUBLE PRECISION,
    product_price    DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS order_address
(
    order_id uuid PRIMARY KEY,
    country  VARCHAR,
    city     VARCHAR,
    street   VARCHAR,
    house    VARCHAR,
    flat     VARCHAR,
    CONSTRAINT fk_primary_key FOREIGN KEY (order_id) REFERENCES "order" (order_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS order_product
(
    product_id uuid PRIMARY KEY,
    count      BIGINT,
    order_id   UUID,
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES "order" (order_id) ON DELETE CASCADE
);