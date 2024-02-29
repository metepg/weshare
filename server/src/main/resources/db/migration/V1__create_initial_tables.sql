CREATE SCHEMA ${schema};

CREATE TABLE ${schema}.person
(
    id       SERIAL PRIMARY KEY,
    username TEXT UNIQUE,
    password TEXT,
    role     TEXT
);

CREATE TABLE ${schema}.bill
(
    id          SERIAL PRIMARY KEY,
    owner       TEXT REFERENCES weshare.person (username),
    amount      NUMERIC(9, 2), -- Max amount 9999,99
    description TEXT,
    category    SMALLINT,
    date        DATE,
    own_amount  NUMERIC(9, 2),
    is_paid     BOOLEAN
);