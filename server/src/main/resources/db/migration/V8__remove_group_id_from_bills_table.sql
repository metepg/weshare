BEGIN;

-- Drop the foreign key constraint on group_id if it exists
ALTER TABLE weshare.bills
    DROP CONSTRAINT IF EXISTS fk_group;

-- Drop the group_id column from the bills table
ALTER TABLE weshare.bills
    DROP COLUMN IF EXISTS group_id;

COMMIT;
