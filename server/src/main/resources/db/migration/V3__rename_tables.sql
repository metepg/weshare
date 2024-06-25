ALTER TABLE ${schema}.person RENAME TO users;
ALTER TABLE ${schema}.bill RENAME TO bills;
-- Update foreign key reference in bills table

ALTER TABLE ${schema}.bills DROP CONSTRAINT bill_owner_fkey;
ALTER TABLE ${schema}.bills ADD CONSTRAINT bills_owner_fkey FOREIGN KEY (owner) REFERENCES ${schema}.users (username);