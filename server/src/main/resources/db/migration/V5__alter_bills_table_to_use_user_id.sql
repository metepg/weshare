BEGIN;

-- Drop the existing fk_owner constraint if it exists
ALTER TABLE weshare.bills
    DROP CONSTRAINT IF EXISTS fk_owner;

-- Add a temporary column for the new owner_id
ALTER TABLE weshare.bills
    ADD COLUMN owner_id INTEGER;

-- Populate the temporary column based on the current owner (username)
UPDATE weshare.bills b
SET owner_id = (SELECT u.id
                FROM weshare.users u
                WHERE u.name = b.owner);

-- Drop the old owner column
ALTER TABLE weshare.bills
    DROP COLUMN owner;

-- Rename the temporary owner_id column to owner
ALTER TABLE weshare.bills
    RENAME COLUMN owner_id TO owner;

-- Rename the is_paid column to paid
ALTER TABLE weshare.bills
    RENAME COLUMN is_paid TO paid;

-- Add foreign key constraint for owner
ALTER TABLE weshare.bills
    ADD CONSTRAINT fk_owner FOREIGN KEY (owner) REFERENCES weshare.users (id);

-- Add group_id column to bills table
ALTER TABLE weshare.bills
    ADD COLUMN group_id UUID;

-- Populate the group_id column in bills based on the user's group_id
UPDATE weshare.bills b
SET group_id = (SELECT u.group_id
                FROM weshare.users u
                WHERE u.id = b.owner);

-- Ensure group_id column is not null
ALTER TABLE weshare.bills
    ALTER COLUMN group_id SET NOT NULL;

-- Add foreign key constraint for group_id
ALTER TABLE weshare.bills
    ADD CONSTRAINT fk_group FOREIGN KEY (group_id) REFERENCES weshare.groups (id);

COMMIT;
