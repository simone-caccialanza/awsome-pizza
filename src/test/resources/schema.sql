CREATE SEQUENCE pizza_order_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE pizza_order (
    id BIGINT DEFAULT NEXTVAL('pizza_order_seq') PRIMARY KEY,
    items TEXT NOT NULL,
    status VARCHAR(255) NOT NULL
);