-- Create transactions and transaction_lines tables
CREATE EXTENSION IF NOT EXISTS pgcrypto;
DROP TABLE IF EXISTS transaction_lines CASCADE;
DROP TABLE IF EXISTS transactions CASCADE;
BEGIN;

CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    uuid UUID NOT NULL DEFAULT gen_random_uuid(),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    currency CHAR(3),
    account_id BIGINT,
    description TEXT
);

-- Indexes to speed queries
CREATE INDEX IF NOT EXISTS idx_transactions_account_id ON transactions(account_id);

CREATE TABLE IF NOT EXISTS transaction_lines (
    id BIGSERIAL PRIMARY KEY,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    uuid UUID NOT NULL DEFAULT gen_random_uuid(),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    transaction_id BIGINT NOT NULL,
    account_id BIGINT,
    debit_amount NUMERIC(18,2) DEFAULT 0,
    credit_amount NUMERIC(18,2) DEFAULT 0,

    CONSTRAINT chk_debit_credit_amount CHECK (
        (debit_amount > 0 AND credit_amount = 0) OR
        (credit_amount > 0 AND debit_amount = 0)
    ),
    CONSTRAINT fk_transaction_lines_account FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE NO ACTION,
    CONSTRAINT fk_transaction_lines_transaction FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_transaction_lines_transaction_id ON transaction_lines(transaction_id);

COMMIT;
