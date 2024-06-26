-- Step 1: Drop the existing fk_owner constraint if it exists
ALTER TABLE weshare.bills DROP CONSTRAINT IF EXISTS fk_owner;

-- Step 2: Add a temporary column for the new owner_id
ALTER TABLE weshare.bills ADD COLUMN owner_id INTEGER;

-- Step 3: Populate the temporary column based on the current owner (username)
UPDATE weshare.bills b
SET owner_id = (
    SELECT u.id
    FROM weshare.users u
    WHERE u.name = b.owner
);

-- Step 4: Drop the old owner column
ALTER TABLE weshare.bills DROP COLUMN owner;

-- Step 5: Rename the temporary owner_id column to owner
ALTER TABLE weshare.bills RENAME COLUMN owner_id TO owner;

-- Step 6: Add foreign key constraint to ensure referential integrity with the users table
ALTER TABLE weshare.bills ADD CONSTRAINT fk_owner FOREIGN KEY (owner) REFERENCES weshare.users (id);
