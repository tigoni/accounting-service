
-- create multi-currency support for accounts and transactions
CREATE TABLE IF NOT EXISTS currencies (
    code CHAR(3) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    symbol VARCHAR(10) NOT NULL UNIQUE
);

-- rename column currency in transactions to base_currency
ALTER TABLE transactions RENAME COLUMN currency TO base_currency;

ALTER TABLE transactions alter COLUMN base_currency SET NOT NULL;


-- Add multi-currency support to transaction_lines
ALTER TABLE transactions ADD COLUMN exchange_rate NUMERIC(18,6) DEFAULT 1.0 NOT NULL;

ALTER TABLE transactions ADD COLUMN transaction_currency CHAR(3) REFERENCES currencies(code) NOT NULL DEFAULT 'USD';


insert into currencies (code, name, symbol) values 
('USD', 'United States Dollar', '$'),
('EUR', 'Euro', '€'),
('GBP', 'British Pound Sterling', '£'),
('JPY', 'Japanese Yen', '¥'),
('KES', 'Kenyan Shilling', 'KSh');