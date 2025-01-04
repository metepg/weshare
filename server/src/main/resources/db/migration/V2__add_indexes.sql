CREATE INDEX idx_categories_group_id ON weshare.categories(group_id);

CREATE INDEX idx_users_group_id ON weshare.users(group_id);

CREATE INDEX idx_bills_category ON weshare.bills(category);

CREATE INDEX idx_bills_owner ON weshare.bills(owner);