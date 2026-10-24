CREATE TABLE IF NOT EXISTS expenses (
    id UUID PRIMARY KEY,
    amount NUMERIC NOT NULL CHECK (amount > 0),
    expense_date DATE NOT NULL,
    category TEXT NOT NULL CHECK (TRIM(category) <> '')
    );