-- Add index on group_id in categories table
CREATE INDEX idx_categories_group_id ON weshare.categories(group_id);

-- Add index on group_id in users table
CREATE INDEX idx_users_group_id ON weshare.users(group_id);

-- Add index on category in bills table
CREATE INDEX idx_bills_category ON weshare.bills(category);

-- Add index on owner in bills table
CREATE INDEX idx_bills_owner ON weshare.bills(owner);
