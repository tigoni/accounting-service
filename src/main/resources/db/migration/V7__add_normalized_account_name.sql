ALTER TABLE accounts ADD COLUMN normalized_name VARCHAR(255);

-- Populate normalized_name for existing accounts
UPDATE accounts SET normalized_name = LOWER(REGEXP_REPLACE(account_name, '[^a-zA-Z0-9\s]', ' ', 'g'));
UPDATE accounts SET normalized_name = REGEXP_REPLACE(normalized_name, '\s+', ' ', 'g');
UPDATE accounts SET normalized_name = TRIM(normalized_name);

-- Create index for faster lookups
CREATE INDEX idx_accounts_normalized_name ON accounts(normalized_name);
