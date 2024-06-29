CREATE TABLE IF NOT EXISTS weshare.categories
(
    id       SERIAL PRIMARY KEY,
    name     TEXT NOT NULL,
    group_id UUID NOT NULL,
    CONSTRAINT fk_group
        FOREIGN KEY (group_id)
            REFERENCES weshare.groups (id)
);

ALTER TABLE weshare.bills
    ADD CONSTRAINT fk_category
        FOREIGN KEY (category)
            REFERENCES weshare.categories (id);
