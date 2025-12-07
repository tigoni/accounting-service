ALTER TABLE transactions 
ADD COLUMN reversed_transaction_id BIGINT NULL,
ADD COLUMN reversed_at TIMESTAMP NULL,
ADD CONSTRAINT fk_transactions_reversed_transaction 
    FOREIGN KEY (reversed_transaction_id) REFERENCES transactions(id) ON DELETE NO ACTION;

CREATE INDEX IF NOT EXISTS idx_transactions_reversed_transaction_id 
    ON transactions(reversed_transaction_id);