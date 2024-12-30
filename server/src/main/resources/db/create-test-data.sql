BEGIN;

-- Insert a new group
INSERT INTO weshare.groups (id, name)
VALUES (gen_random_uuid(), 'Test Group');

-- Retrieve the group ID
INSERT INTO weshare.users (id, name, password, role, group_id)
VALUES
    (1, 'user', '$2a$10$aBNsZVVWtDI0ZxcYue/30ebE0qsL7qT49uhxEvU1xJ3lp9GHVgSD6', 'Role1',
     (SELECT id FROM weshare.groups WHERE name = 'Test Group')),
    (2, 'user2', '$2a$10$uM20SAvEWY9X1b5wVEoZ3uBv0Xr8ucHatGZgGowEJ9ETWwZZWseaW', 'Role2',
     (SELECT id FROM weshare.groups WHERE name = 'Test Group'));

-- Insert categories linked to the group
INSERT INTO weshare.categories (id, name, group_id)
VALUES
    (0, 'Auto', (SELECT id FROM weshare.groups WHERE name = 'Test Group')),
    (1, 'Kissat', (SELECT id FROM weshare.groups WHERE name = 'Test Group')),
    (2, 'Laskut', (SELECT id FROM weshare.groups WHERE name = 'Test Group')),
    (3, 'Ravintola', (SELECT id FROM weshare.groups WHERE name = 'Test Group')),
    (4, 'Ruoka', (SELECT id FROM weshare.groups WHERE name = 'Test Group')),
    (5, 'Muut', (SELECT id FROM weshare.groups WHERE name = 'Test Group')),
    (6, 'Koti', (SELECT id FROM weshare.groups WHERE name = 'Test Group')),
    (7, 'Nollaus', (SELECT id FROM weshare.groups WHERE name = 'Test Group'));

-- Insert 1000 bills with random data
INSERT INTO weshare.bills (owner, amount, description, category, date, own_amount, paid)
SELECT
    1 + floor(random() * 2)::int AS owner,
    (2000 + floor(random() * 8000))::numeric AS amount,
    CASE mod(generate_series, 6)
        WHEN 0 THEN 'Food'
        WHEN 1 THEN 'Gasoline'
        WHEN 2 THEN 'Food for cats'
        WHEN 3 THEN 'Electric bill'
        WHEN 4 THEN 'Water bill'
        WHEN 5 THEN 'Walmart'
        END AS description,
    mod(generate_series, 6) AS category,
    NOW() - (generate_series * INTERVAL '1 day') AS date,
    (2000 + floor(random() * 8000)) * (mod(generate_series, 11) * 10) / 100 AS own_amount,
    generate_series < 980
FROM generate_series(1, 1000);

COMMIT;
