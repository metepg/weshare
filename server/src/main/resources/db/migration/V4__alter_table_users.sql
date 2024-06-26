-- Rename the username column to name
ALTER TABLE weshare.users RENAME COLUMN username TO name;

-- Add the group_id column to users table
ALTER TABLE weshare.users ADD COLUMN group_id UUID;

-- Enable the uuid-ossp extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Generate a single UUID for the group and assign it to the two existing users
DO $$
    DECLARE
        group_uuid UUID := uuid_generate_v4();
    BEGIN
        -- Populate the group_id column with the same UUID for the two existing users
        UPDATE weshare.users SET group_id = group_uuid;
    END $$;

-- Ensure group_id column is not null
ALTER TABLE weshare.users ALTER COLUMN group_id SET NOT NULL;

-- Add a unique constraint on the combination of name and group_id
ALTER TABLE weshare.users
    ADD CONSTRAINT unique_name_group_id UNIQUE (name, group_id);
