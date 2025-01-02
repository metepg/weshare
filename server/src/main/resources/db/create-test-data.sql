BEGIN;

-- Groups
INSERT INTO weshare.groups (id, name)
SELECT gen_random_uuid(), 'Test Group'
WHERE NOT EXISTS (
    SELECT 1
    FROM weshare.groups
    WHERE name = 'Test Group'
);

-- Users
INSERT INTO weshare.users (id, name, password, role, group_id)
SELECT 1, 'user',
       '$2a$10$aBNsZVVWtDI0ZxcYue/30ebE0qsL7qT49uhxEvU1xJ3lp9GHVgSD6',
       'Role1',
       (SELECT id FROM weshare.groups WHERE name = 'Test Group')
WHERE NOT EXISTS (
    SELECT 1
    FROM weshare.users
    WHERE id = 1
);

INSERT INTO weshare.users (id, name, password, role, group_id)
SELECT 2, 'user2',
       '$2a$10$uM20SAvEWY9X1b5wVEoZ3uBv0Xr8ucHatGZgGowEJ9ETWwZZWseaW',
       'Role2',
       (SELECT id FROM weshare.groups WHERE name = 'Test Group')
WHERE NOT EXISTS (
    SELECT 1
    FROM weshare.users
    WHERE id = 2
);

-- Categories
INSERT INTO weshare.categories (id, name, group_id)
SELECT c.id, c.name,
       (SELECT id FROM weshare.groups WHERE name = 'Test Group')
FROM (VALUES
          (0, 'Auto'),
          (1, 'Kissat'),
          (2, 'Laskut'),
          (3, 'Ravintola'),
          (4, 'Ruoka'),
          (5, 'Muut'),
          (6, 'Koti'),
          (7, 'Nollaus')
     ) AS c(id, name)
WHERE NOT EXISTS (
    SELECT 1
    FROM weshare.categories
);

-- Bills
INSERT INTO weshare.bills (owner, amount, description, category, date, own_amount, paid)
SELECT
    (1 + floor(random() * 2)::int),
    (2000 + floor(random() * 8000))::numeric,
    CASE mod(generate_series, 6)
        WHEN 0 THEN 'Food'
        WHEN 1 THEN 'Gasoline'
        WHEN 2 THEN 'Food for cats'
        WHEN 3 THEN 'Electric bill'
        WHEN 4 THEN 'Water bill'
        WHEN 5 THEN 'Walmart'
        END,
    mod(generate_series, 6),
    NOW() - (generate_series * INTERVAL '1 day'),
    (2000 + floor(random() * 8000)) * (mod(generate_series, 11) * 10) / 100,
    generate_series < 980
FROM generate_series(1, 1000) generate_series
WHERE NOT EXISTS (
    SELECT 1
    FROM weshare.bills);

COMMIT;
