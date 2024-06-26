BEGIN;

-- Insert a new group and capture its generated ID
DO $$
    DECLARE
        group_uuid UUID;
    BEGIN
        INSERT INTO weshare.groups (name) VALUES ('Test Group') RETURNING id INTO group_uuid;

        -- Insert initial user records with the same group_id
        INSERT INTO weshare.users (id, name, password, role, group_id)
        VALUES
            (1, 'user', '$2a$10$aBNsZVVWtDI0ZxcYue/30ebE0qsL7qT49uhxEvU1xJ3lp9GHVgSD6', 'Role1', group_uuid),
            (2, 'user2', '$2a$10$uM20SAvEWY9X1b5wVEoZ3uBv0Xr8ucHatGZgGowEJ9ETWwZZWseaW', 'Role2', group_uuid);

        -- Insert categories
        INSERT INTO weshare.categories (id, name, group_id)
        VALUES
               (0, 'Auto', group_uuid),
               (1, 'Kissat', group_uuid),
               (2, 'Laskut', group_uuid),
               (3, 'Ravintola', group_uuid),
               (4, 'Ruoka', group_uuid),
               (5, 'Muut', group_uuid),
               (6, 'Koti', group_uuid),
               (7, 'Nollaus', group_uuid);
    END $$;

-- Generate 1000 bills between last year and this year with random owners
DO $$
    DECLARE
        descriptions TEXT[] := ARRAY ['Food', 'Gasoline', 'Food for cats', 'Electric bill', 'Water bill', 'Walmart'];
        owner        INTEGER;
        amount       NUMERIC;
        description  TEXT;
        category     INTEGER;
        date         TIMESTAMP;
        own_amount   NUMERIC;
        paid         BOOLEAN;
        i            INT;
        percentage   INTEGER;
    BEGIN
        FOR i IN 1..1000 LOOP
                -- Randomly select owner (1 or 2)
                owner := 1 + floor(random() * 2)::int;

                -- Generate amount in cents (range 2000 to 10000 cents)
                amount := (20 + (floor(random() * 9) * 10)) * 100;

                description := descriptions[1 + (i % array_length(descriptions, 1))];
                category := floor(random() * 6);
                date := NOW() - ((1000 - i) * INTERVAL '1 day');

                -- Generate percentage in steps of 10% (0, 10, 20, ..., 100)
                percentage := floor(random() * 11) * 10;

                -- Calculate own_amount as percentage of amount
                own_amount := (amount * percentage) / 100;

                paid := i < 980;

                INSERT INTO weshare.bills (owner, amount, description, category, date, own_amount, paid)
                VALUES (owner, amount, description, category, date, own_amount, paid);
            END LOOP;
    END $$;

COMMIT;
