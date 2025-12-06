-- Create extension for UUID generation
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Drop tables if they exist
DROP TABLE IF EXISTS account_categories CASCADE;
DROP TABLE IF EXISTS account_types CASCADE;
DROP TABLE IF EXISTS accounts CASCADE;


/*Categories of accounts i.e Assets, liabilities, equity, revenue, expenses */
CREATE TABLE IF NOT EXISTS account_categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

/*Types of accounts i.e Current Assets, Non-current Assets, Current Liabilities, Non-current Liabilities, Equity, Revenue, Expenses */
CREATE TABLE IF NOT EXISTS  account_types  (
    account_id INT  PRIMARY KEY,
        category_id INT REFERENCES account_categories(id) ON DELETE CASCADE ON UPDATE CASCADE,
        account_number VARCHAR(3) NOT NULL UNIQUE,
    account_name VARCHAR(255) NOT NULL,
    account_description TEXT
);


-- Create the account table
CREATE TABLE IF NOT EXISTS accounts (
    id BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(255) NOT NULL,
    account_name VARCHAR(255) NOT NULL,
    account_type_id BIGINT NOT NULL,
    account_description TEXT,
    created_by VARCHAR(255) NOT NULL,
    uuid VARCHAR(255) NOT NULL,
    parent_account_id BIGINT,
    CONSTRAINT fk_parent_account_id FOREIGN KEY (parent_account_id) REFERENCES accounts(id),
    CONSTRAINT fk_account_type_id FOREIGN KEY (account_type_id) REFERENCES account_types(account_id) ON DELETE CASCADE
);

