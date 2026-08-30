CREATE TABLE IF NOT EXISTS contact_attribute_values
(
    contact_id uuid,
    attribute  character varying NOT NULL,
    value      character varying NOT NULL,
    primary key (contact_id, attribute, value),
        FOREIGN KEY (contact_id)
        REFERENCES contacts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);