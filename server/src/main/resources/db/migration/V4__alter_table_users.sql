BEGIN;

-- Enable the uuid-ossp extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create the groups table
CREATE TABLE weshare.groups
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name       TEXT,
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

DO
$$
    DECLARE
        new_group_id UUID;
    BEGIN
        INSERT INTO weshare.groups (name) VALUES ('Default Group') RETURNING id INTO new_group_id;

        -- Rename the username column to name in the users table
        ALTER TABLE weshare.users
            RENAME COLUMN username TO name;

        -- Add the group_id column to users table
        ALTER TABLE weshare.users
            ADD COLUMN group_id UUID;

        -- Populate the group_id column with the new group_id for the existing users
        UPDATE weshare.users SET group_id = new_group_id;

        -- Ensure group_id column is not null
        ALTER TABLE weshare.users
            ALTER COLUMN group_id SET NOT NULL;

        -- Add a unique constraint on the combination of name and group_id
        ALTER TABLE weshare.users
            ADD CONSTRAINT unique_name_group_id UNIQUE (name, group_id);
    END
$$;

COMMIT;
