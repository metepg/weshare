ALTER TABLE weshare.bills
    DROP CONSTRAINT fk_category,
    ADD CONSTRAINT fk_category FOREIGN KEY (category)
        REFERENCES weshare.categories (id) ON UPDATE CASCADE;
