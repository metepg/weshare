-- Better to have currency values stored as whole numbers
ALTER TABLE weshare.bill
    ALTER COLUMN amount TYPE INTEGER USING (amount * 100)::INTEGER,
    ALTER COLUMN own_amount TYPE INTEGER USING (own_amount * 100)::INTEGER;
