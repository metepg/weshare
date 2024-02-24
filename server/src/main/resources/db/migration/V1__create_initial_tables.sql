CREATE TABLE weshare.bill
(
    id          SERIAL PRIMARY KEY,
    owner       TEXT,
    amount      NUMERIC(9, 2), -- Max amount 9999,99
    description TEXT,
    category    SMALLINT,
    date        TIMESTAMP,
    own_amount  NUMERIC(9, 2),
    is_paid     BOOLEAN
);

CREATE TABLE weshare.person
(
    id       SERIAL PRIMARY KEY,
    username TEXT UNIQUE,
    password TEXT,
    role     TEXT
);
