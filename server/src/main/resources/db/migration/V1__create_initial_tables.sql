CREATE SCHEMA weshare;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE weshare.groups
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    name       TEXT
);

CREATE TABLE weshare.users
(
    id       SERIAL PRIMARY KEY,
    group_id UUID,
    name     TEXT UNIQUE,
    password TEXT,
    role     TEXT,
    CONSTRAINT unique_name_group_id UNIQUE (id, group_id)
);

CREATE TABLE weshare.categories
(
    id       SERIAL PRIMARY KEY,
    name     TEXT NOT NULL,
    group_id UUID NOT NULL,
    CONSTRAINT fk_group FOREIGN KEY (group_id) REFERENCES weshare.groups (id)
);

CREATE TABLE weshare.bills
(
    id          SERIAL PRIMARY KEY,
    amount      INTEGER CHECK (amount <= 999999), -- Max amount 9999,99
    category    SMALLINT CONSTRAINT fk_category REFERENCES weshare.categories (id),
    date        DATE,
    description TEXT,
    paid        BOOLEAN,
    own_amount  INTEGER CHECK (amount <= 999999), -- Max amount 9999,99
    owner       INTEGER
        CONSTRAINT fk_owner REFERENCES weshare.users (id)
);