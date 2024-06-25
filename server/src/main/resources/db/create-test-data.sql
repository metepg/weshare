BEGIN;

-- Insert initial user records
INSERT INTO weshare.users (username, password, role)
VALUES ('user', '$2a$10$aBNsZVVWtDI0ZxcYue/30ebE0qsL7qT49uhxEvU1xJ3lp9GHVgSD6', 'Role1'),
       ('user2', '$2a$10$uM20SAvEWY9X1b5wVEoZ3uBv0Xr8ucHatGZgGowEJ9ETWwZZWseaW', 'Role2');

-- Generate 1000 bills between last year and this year with random owners
DO
$$
    DECLARE
        ownerNames   TEXT[] := ARRAY ['user', 'user2'];
        descriptions TEXT[] := ARRAY ['Food', 'Gasoline', 'Food for cats', 'Electric bill', 'Water bill', 'Walmart'];
        owner        TEXT;
        amount       NUMERIC;
        description  TEXT;
        category     INTEGER;
        date         TIMESTAMP;
        own_amount   NUMERIC;
        is_paid      BOOLEAN;
        i            INT;
        percentage   INTEGER;
    BEGIN
        FOR i IN 1..1000
            LOOP
                owner := ownerNames[1 + (i % array_length(ownerNames, 1))];

                -- Generate amount in cents (range 2000 to 10000 cents)
                amount := (20 + (floor(random() * 9) * 10)) * 100;

                description := descriptions[1 + (i % array_length(descriptions, 1))];
                category := floor(random() * 6);
                date := NOW() - ((1000 - i) * INTERVAL '1 day');

                -- Generate percentage in steps of 10% (0, 10, 20, ..., 100)
                percentage := floor(random() * 11) * 10;

                -- Calculate own_amount as percentage of amount
                own_amount := (amount * percentage) / 100;

                is_paid := i < 980;

                INSERT INTO weshare.bills (owner, amount, description, category, date, own_amount, is_paid)
                VALUES (owner, amount, description, category, date, own_amount, is_paid);
            END LOOP;
    END
$$;

COMMIT;
