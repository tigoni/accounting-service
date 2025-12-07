CREATE INDEX IF NOT EXISTS idx_transaction_lines_account_id ON transaction_lines(account_id);
CREATE INDEX IF NOT EXISTS idx_transaction_lines_created_at ON transaction_lines(created_at);
CREATE INDEX IF NOT EXISTS idx_transaction_lines_account_created_at ON transaction_lines(account_id, created_at);
CREATE INDEX IF NOT EXISTS idx_transactions_reversed_at ON transactions(reversed_at);


