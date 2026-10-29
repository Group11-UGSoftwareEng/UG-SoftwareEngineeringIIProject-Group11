/* Comment out the bottom two lines if wish to reset database.
DROP TABLE IF EXISTS expense CASCADE;
DROP TABLE IF EXISTS category CASCADE;
 */

CREATE TABLE IF NOT EXISTS category
(
    id     UUID         NOT NULL,
    budget DOUBLE PRECISION,
    name   VARCHAR(255) NOT NULL,
    year   INTEGER      NOT NULL,
    month  INTEGER      NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uc_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS expense
(
    id          UUID             NOT NULL,
    category_id UUID             NOT NULL,
    amount      DOUBLE PRECISION NOT NULL,
    description VARCHAR(255),
    currency    VARCHAR(255)     NOT NULL,
    date        date             NOT NULL,
    CONSTRAINT pk_expense PRIMARY KEY (id),
    CONSTRAINT FK_EXPENSE_ON_CATEGORY
    FOREIGN KEY (category_id) REFERENCES category (id)
);

/* SAMPLE DATA, TO BE REMOVED LATER */

INSERT INTO category (id, budget, name, year, month)
VALUES


    (
        '3f6a1b2c-9d84-4e71-a6c3-5b8f02d9471a',
        400.00,
        'Sample category 2',
        2026,
        10
    ),
    (
        '7f3a9c21-8d64-4e2b-a5f0-1c9e6b72d843',
        400.00,
        'Sample category 1',
        2026,
        10
    ),
    (
        '22222222-2222-4222-8222-222222222222',
        150.00,
        'Transport',
        2026,
        10
    ),
    (
        '11111111-1111-4111-8111-111111111111',
        400.00,
        'Groceries',
        2026,
        10
    )
    ON CONFLICT DO NOTHING;


INSERT INTO expense
(id, category_id, amount, description, currency, date)
VALUES
    (
        'aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa',
        '11111111-1111-4111-8111-111111111111',
        65.50,
        'Weekly supermarket shopping',
        'EUR',
        '2026-10-28'
    ),
    (
        'bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbbb',
        '7f3a9c21-8d64-4e2b-a5f0-1c9e6b72d843',
        25.00,
        'Groceries',
        'EUR',
        '2026-10-29'
    )
    ON CONFLICT DO NOTHING;