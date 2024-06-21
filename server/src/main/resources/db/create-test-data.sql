-- -- Generate 1000 bills between last year and this year with random owners
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
    BEGIN
        FOR i IN 1..1000
            LOOP
                owner := ownerNames[1 + (i % array_length(ownerNames, 1))];
                amount := 20 + (random() * (100 - 20));
                description := descriptions[1 + (i % array_length(descriptions, 1))];
                category := floor(random() * 6);
                date := NOW() - ((1000 - i) * INTERVAL '1 day');
                own_amount := random() * 80;
                is_paid := i < 980;

                INSERT INTO weshare.bill (owner, amount, description, category,date, own_amount, is_paid)
                VALUES (owner, amount, description, category, date, own_amount,is_paid);
            END LOOP;
    END
$$;