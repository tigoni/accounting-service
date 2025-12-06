BEGIN;
-- Migration: Populate template tables
INSERT INTO account_categories (name, description) VALUES
('Assets', 'Resources owned by entity.'),
('Liabilities', 'Obligations owed by the entity.'),
('Equity', 'Owner’s equity'),
('Income', 'Revenue earned by the entity.'),
('Expenses', 'Costs incurred by the entity.');

INSERT INTO account_types (account_id, category_id, account_number, account_name, account_description) VALUES
-- Asset Types
(1, 1, '101', 'Current Assets', 'Assets that can be converted to cash within a year.'),
(2, 1, '102', 'Fixed Assets', 'Long-term assets used in operations.'),
-- Liability Types
(3, 2, '201','Current Liabilities', 'Obligations due within a year.'),
(4, 2, '202','Long-term Liabilities', 'Obligations due beyond a year.'),
-- Equity Types
(5, 3, '301','Equity', 'Contributions, capital and, retained earnings.'),
-- Revenue Types
(6, 4, '401','Operating Income', 'Primary income from operations.'),
(7, 4, '402','Non-operating Income', 'Secondary income like interest income.'),
-- Expense Types
(8, 5, '501','Operating Expenses', 'Costs related to core operations.'),
(9, 5, '502','Non-operating Expenses', 'Costs like interest expenses.');


--Assets (Account Type 1 = Current Assets, Type 2 = Fixed Assets)
INSERT INTO accounts (account_number, account_name, account_type_id, account_description, created_by, uuid, to_publish_on_event)
VALUES 
    -- Cash and Cash Equivalents
    ('101-01-XX', 'Cash', 1, 'Parent account for all cash and cash equivalent accounts', 'system', gen_random_uuid(), FALSE),
    ('101-03-XX', 'Cash in Bank - Operating Account', 1, 'Main operating bank account', 'system', gen_random_uuid(), FALSE),
    ('101-04-XX', 'Cash in Bank - Savings Account', 1, 'Savings and reserve bank account', 'system', gen_random_uuid(), FALSE),
    ('101-05-XX', 'M-Pesa Account', 1, 'Mobile money account balance', 'system', gen_random_uuid(), FALSE);
    
    -- Receivables
    ('101-10-XX', 'Accounts Receivable', 1, 'Amounts owed by customers for goods/services', 'system', gen_random_uuid(), FALSE),
    ('101-11-XX', 'Loans Receivable - Short Term', 1, 'Loans to be collected within one year', 'system', gen_random_uuid(), FALSE),
    ('101-12-XX', 'Member Loans Receivable', 1, 'Loans disbursed to members', 'system', gen_random_uuid(), FALSE),
    ('101-13-XX', 'Interest Receivable', 1, 'Accrued interest not yet collected', 'system', gen_random_uuid(), FALSE),
    

-- Liabilities (Account Type 3 = Current Liabilities, Type 4 = Long-term Liabilities)
INSERT INTO accounts (account_number, account_name, account_type_id, account_description, created_by, uuid, to_publish_on_event)
VALUES 
    -- Payables
    ('201-01-XX', 'Accounts Payable', 3, 'Amounts owed to suppliers and vendors', 'system', gen_random_uuid(), FALSE),
    ('201-02-XX', 'Accrued Expenses', 3, 'Expenses incurred but not yet paid', 'system', gen_random_uuid(), FALSE),
    ('201-03-XX', 'Accrued Salaries and Wages', 3, 'Salaries and wages owed to employees', 'system', gen_random_uuid(), FALSE),
    ('201-04-XX', 'Accrued Interest Payable', 3, 'Interest owed but not yet paid', 'system', gen_random_uuid(), FALSE),
    
    -- Other Current Liabilities
    ('201-20-XX', 'Unearned Revenue', 3, 'Payments received for services not yet rendered', 'system', gen_random_uuid(), FALSE),
    ('201-21-XX', 'Taxes Payable', 3, 'Taxes owed to government authorities', 'system', gen_random_uuid(), FALSE),
    ('201-22-XX', 'Deposits and Advances', 3, 'Customer deposits and advances received', 'system', gen_random_uuid(), FALSE),
    ('201-23-XX', 'Member Savings Deposits', 3, 'Savings deposits from members', 'system', gen_random_uuid(), FALSE);

-- Long-term Liabilities (Account Type 4)
INSERT INTO accounts (account_number, account_name, account_type_id, account_description, created_by, uuid, to_publish_on_event)
VALUES 
    ('202-01-XX', 'Long Term Loans Payable', 4, 'Loans payable beyond one year', 'system', gen_random_uuid(), FALSE),
    ('202-02-XX', 'Lender Capital', 4, 'Capital provided by external lenders', 'system', gen_random_uuid(), FALSE),
    ('202-05-XX', 'Deferred Tax Liability', 4, 'Tax liabilities deferred to future periods', 'system', gen_random_uuid(), FALSE);

-- Equity (Account Type 5)
INSERT INTO accounts (account_number, account_name, account_type_id, account_description, created_by, uuid, to_publish_on_event)
VALUES 
    ('301-01-XX', 'Owner''s Equity', 5, 'Owner''s investment in the business', 'system', gen_random_uuid(), FALSE),
    ('301-02-XX', 'Share Capital', 5, 'Capital raised through share issuance', 'system', gen_random_uuid(), FALSE),
    ('301-03-XX', 'Member Contributions', 5, 'Member capital contributions', 'system', gen_random_uuid(), FALSE),
    ('301-04-XX', 'Retained Earnings', 5, 'Accumulated profits retained in the business', 'system', gen_random_uuid(), FALSE),

 -- Income (Account Type 6 = Operating Income, Type 7 = Non-operating Income)
INSERT INTO accounts (account_number, account_name, account_type_id, account_description, created_by, uuid, to_publish_on_event)
VALUES 
    -- Interest Income
    ('401-01-XX', 'Interest Income - Loans', 6, 'Interest earned from loans disbursed', 'system', gen_random_uuid(), FALSE),
    ('401-02-XX', 'Interest Income - Investments', 6, 'Interest earned from investments', 'system', gen_random_uuid(), FALSE),
    ('401-03-XX', 'Interest Income - Bank Deposits', 6, 'Interest earned from bank deposits', 'system', gen_random_uuid(), FALSE),
    
    -- Fee Income
    ('401-10-XX', 'Fee Income - Service Charges', 6, 'Fees charged for services rendered', 'system', gen_random_uuid(), FALSE),
    ('401-11-XX', 'Fee Income - Loan Processing', 6, 'Fees from loan processing and origination', 'system', gen_random_uuid(), FALSE),
    ('401-12-XX', 'Fee Income - Late Payment Penalties', 6, 'Penalties charged for late payments', 'system', gen_random_uuid(), FALSE),
    ('401-13-XX', 'Fee Income - Membership Fees', 6, 'Membership and subscription fees', 'system', gen_random_uuid(), FALSE),
    ('401-14-XX', 'Fee Income - Transaction Fees', 6, 'Fees from various transactions', 'system', gen_random_uuid(), FALSE),
    

-- Expenses (Account Type 8 = Operating Expenses, Type 9 = Non-operating Expenses)
INSERT INTO accounts (account_number, account_name, account_type_id, account_description, created_by, uuid, to_publish_on_event)
VALUES 
    -- Personnel Expenses
    ('501-01-XX', 'Salaries and Wages', 8, 'Employee salaries and wages', 'system', gen_random_uuid(), FALSE),
    ('501-03-XX', 'Payroll Taxes', 8, 'Employer portion of payroll taxes', 'system', gen_random_uuid(), FALSE),
    
    -- Operating Expenses
    ('501-10-XX', 'Rent Expense', 8, 'Office and facility rent', 'system', gen_random_uuid(), FALSE),
    ('501-11-XX', 'Utilities Expense', 8, 'Electricity, water, gas, internet, etc.', 'system', gen_random_uuid(), FALSE),
   
    -- Financial Expenses
    ('501-60-XX', 'Interest Expense', 8, 'Interest paid on borrowings', 'system', gen_random_uuid(), FALSE),
    ('501-61-XX', 'Bad Debt Expense', 8, 'Losses from uncollectible receivables', 'system', gen_random_uuid(), FALSE),
    ('501-62-XX', 'Loan Loss Provision', 8, 'Provision for potential loan losses', 'system', gen_random_uuid(), FALSE),
    
    -- Other Expenses
    ('501-93-XX', 'Miscellaneous Expenses', 8, 'Other sundry expenses', 'system', gen_random_uuid(), FALSE);


UPDATE accounts 
SET child_account_id = (SELECT id FROM accounts WHERE account_number = '101-01-01' LIMIT 1)
WHERE account_number = '101-01-XX';

UPDATE accounts 
SET child_account_id = (SELECT id FROM accounts WHERE account_number = '101-03-01' LIMIT 1)
WHERE account_number = '101-03-XX';
COMMIT;