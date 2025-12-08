ALTER TABLE transactions ADD COLUMN uuid UUID;

ALTER TABLE transactions ALTER COLUMN uuid SET NOT NULL;
ALTER TABLE transactions ADD CONSTRAINT uk_transactions_uuid UNIQUE (uuid);

CREATE INDEX IF NOT EXISTS idx_transactions_uuid ON transactions(uuid);

