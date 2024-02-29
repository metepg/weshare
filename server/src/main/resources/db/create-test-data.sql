-- -- Generate 1000 bills between last year and this year with random owners
DO
$$
    DECLARE
        ownerNames   TEXT[] := ARRAY ['user', 'user2'];
        descriptions TEXT[] := ARRAY ['Food', 'Gasoline', 'Food for cats', 'Electric bill', 'Water bill', 'Walmart'];
        i            INT;
    BEGIN
        FOR i IN 1..1000 LOOP
                INSERT INTO weshare.bill (owner, amount, description, category, date, own_amount, is_paid)
                VALUES (ownerNames[1 + (i % array_length(ownerNames, 1))],
                        20 + (random() * (100 - 20)),
                        descriptions[1 + (i % array_length(descriptions, 1))],
                        floor(random() * 5) + 1,
                        NOW() - ((1000 - i) * INTERVAL '1 day'),
                        random() * 80,
                        i < 980);
            END LOOP;
    END
$$;
