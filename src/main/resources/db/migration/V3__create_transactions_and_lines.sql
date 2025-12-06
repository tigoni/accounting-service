-- Create transactions and transaction_lines tables
CREATE EXTENSION IF NOT EXISTS pgcrypto;

BEGIN;

CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    uuid UUID NOT NULL DEFAULT gen_random_uuid(),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    transaction_date TIMESTAMP NOT NULL,
    transaction_reference VARCHAR(255) UNIQUE,
    currency CHAR(3),
    account_id BIGINT,
    description TEXT,
    status VARCHAR(255),
    amount NUMERIC(18,2) NOT NULL DEFAULT 0,

    CONSTRAINT fk_account_id_transactions FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE NO ACTION
);

-- Indexes to speed queries
CREATE INDEX IF NOT EXISTS idx_transactions_transaction_date ON transactions(transaction_date);
CREATE INDEX IF NOT EXISTS idx_transactions_account_id ON transactions(account_id);

CREATE TABLE IF NOT EXISTS transaction_lines (
    id BIGSERIAL PRIMARY KEY,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    uuid UUID NOT NULL DEFAULT gen_random_uuid(),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    to_publish_on_event BOOLEAN DEFAULT FALSE,

    line_description TEXT,
    transaction_id BIGINT NOT NULL,
    debit_amount NUMERIC(18,2) DEFAULT 0,
    credit_amount NUMERIC(18,2) DEFAULT 0,

    CONSTRAINT fk_transaction_lines_transaction FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_transaction_lines_transaction_id ON transaction_lines(transaction_id);

COMMIT;
