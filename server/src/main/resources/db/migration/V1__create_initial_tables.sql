CREATE SCHEMA weshare;

-- Enable the uuid-ossp extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create the groups table
CREATE TABLE weshare.groups
(
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name       TEXT
);

-- Create the users table
CREATE TABLE weshare.users
(
    group_id UUID,
    id       SERIAL PRIMARY KEY,
    name     TEXT UNIQUE,
    password TEXT,
    role     TEXT,
    CONSTRAINT unique_name_group_id UNIQUE (id, group_id)
);

-- Create the categories table
CREATE TABLE weshare.categories
(
    id       SERIAL PRIMARY KEY,
    name     TEXT NOT NULL,
    group_id UUID NOT NULL,
    CONSTRAINT fk_group FOREIGN KEY (group_id) REFERENCES weshare.groups (id)
);

-- Create the bills table
CREATE TABLE weshare.bills
(
    amount      INTEGER CHECK (amount <= 999999), -- Max amount 9999,99
    category    SMALLINT CONSTRAINT fk_category REFERENCES weshare.categories (id),
    date        DATE,
    description TEXT,
    id          SERIAL PRIMARY KEY,
    paid        BOOLEAN,
    own_amount  INTEGER
        CHECK (amount <= 999999), -- Max amount 9999,99
    owner       INTEGER
        CONSTRAINT fk_owner REFERENCES weshare.users (id)
);

