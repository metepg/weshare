ALTER TABLE weshare.person RENAME TO users;
ALTER TABLE weshare.bill RENAME TO bills;
-- Update foreign key reference in bills table

ALTER TABLE weshare.bills DROP CONSTRAINT bill_owner_fkey;
ALTER TABLE weshare.bills ADD CONSTRAINT bills_owner_fkey FOREIGN KEY (owner) REFERENCES weshare.users (username);