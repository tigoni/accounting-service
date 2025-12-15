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


-- Assets (Account Type 1 = Current Assets, Type 2 = Fixed Assets)
INSERT INTO accounts (account_number, account_name, account_type_id, parent_account_id, account_description, created_by)
VALUES 
    -- Cash and Cash Equivalents
    ('101-01-XX', 'Cash', 1, NULL, 'Parent account for all cash and cash equivalent accounts', 'system'),
    ('101-03-XX', 'Cash in Bank - Operating Account', 1, 1, 'Main operating bank account', 'system'),
    ('101-04-XX', 'Cash in Bank - Savings Account', 1, 1, 'Savings and reserve bank account', 'system'),
    ('101-05-XX', 'M-Pesa Account', 1, 1, 'Mobile money account balance', 'system'),
    
    -- Receivables
    ('101-10-XX', 'Accounts Receivable', 1, NULL, 'Amounts owed by customers for goods/services', 'system'),
    ('101-11-XX', 'Loans Receivable - Short Term', 1, 1, 'Loans to be collected within one year', 'system'),
    ('101-12-XX', 'Member Loans Receivable', 1, 1, 'Loans disbursed to members', 'system'),
    ('101-13-XX', 'Interest Receivable', 1, 1, 'Accrued interest not yet collected', 'system'),
    ('101-14-XX', 'Loan Origination Fee Receivable', 1, 1, 'Loan Fees not yet collected', 'system');



-- Liabilities (Account Type 3 = Current Liabilities, Type 4 = Long-term Liabilities)
INSERT INTO accounts (account_number, account_name, account_type_id, parent_account_id, account_description, created_by)
VALUES 
    -- Payables
    ('201-01-XX', 'Accounts Payable', 3, NULL, 'Amounts owed to suppliers and vendors', 'system'),
    ('201-02-XX', 'Accrued Expenses', 3, NULL, 'Expenses incurred but not yet paid', 'system'),
    ('201-03-XX', 'Accrued Salaries and Wages', 3, NULL, 'Salaries and wages owed to employees', 'system'),
    ('201-04-XX', 'Accrued Interest Payable', 3, NULL, 'Interest owed but not yet paid', 'system'),
    
    -- Other Current Liabilities
    ('201-20-XX', 'Unearned Revenue', 3, NULL, 'Payments received for services not yet rendered', 'system'),
    ('201-21-XX', 'Taxes Payable', 3, NULL, 'Taxes owed to government authorities', 'system'),
    ('201-22-XX', 'Deposits and Advances', 3, NULL, 'Customer deposits and advances received', 'system'),
    ('201-23-XX', 'Member Savings Deposits', 3, NULL, 'Savings deposits from members', 'system');


-- Long-term Liabilities (Account Type 4)
INSERT INTO accounts (account_number, account_name, account_type_id, parent_account_id, account_description, created_by)
VALUES 
    ('202-01-XX', 'Long Term Loans Payable', 4, NULL, 'Loans payable beyond one year', 'system'),
    ('202-02-XX', 'Lender Capital', 4, NULL, 'Capital provided by external lenders', 'system'),
    ('202-05-XX', 'Deferred Tax Liability', 4, NULL, 'Tax liabilities deferred to future periods', 'system');


-- Equity (Account Type 5)
INSERT INTO accounts (account_number, account_name, account_type_id, parent_account_id, account_description, created_by)
VALUES 
    ('301-01-XX', 'Owner''s Equity', 5, NULL, 'Owner''s investment in the business', 'system'),
    ('301-02-XX', 'Share Capital', 5, NULL, 'Capital raised through share issuance', 'system'),
    ('301-03-XX', 'Member Contributions', 5, NULL, 'Member capital contributions', 'system'),
    ('301-04-XX', 'Retained Earnings', 5, NULL, 'Accumulated profits retained in the business', 'system');


-- Income (Account Type 6 = Operating Income, Type 7 = Non-operating Income)
INSERT INTO accounts (account_number, account_name, account_type_id, parent_account_id, account_description, created_by)
VALUES 
    -- Interest Income
    ('401-01-XX', 'Interest Income - Loans', 6, NULL, 'Interest earned from loans disbursed', 'system'),
    ('401-02-XX', 'Interest Income - Investments', 6, NULL, 'Interest earned from investments', 'system'),
    ('401-03-XX', 'Interest Income - Bank Deposits', 6, NULL, 'Interest earned from bank deposits', 'system'),
    
    -- Fee Income
    ('401-10-XX', 'Fee Income - Service Charges', 6, NULL, 'Fees charged for services rendered', 'system'),
    ('401-11-XX', 'Fee Income - Loan Processing', 6, NULL, 'Fees from loan processing and origination', 'system'),
    ('401-12-XX', 'Fee Income - Late Payment Penalties', 6, NULL, 'Penalties charged for late payments', 'system'),
    ('401-13-XX', 'Fee Income - Membership Fees', 6, NULL, 'Membership and subscription fees', 'system'),
    ('401-14-XX', 'Fee Income - Transaction Fees', 6, NULL, 'Fees from various transactions', 'system');


-- Expenses (Account Type 8 = Operating Expenses, Type 9 = Non-operating Expenses)
INSERT INTO accounts (account_number, account_name, account_type_id, parent_account_id, account_description, created_by)
VALUES 
    -- Personnel Expenses
    ('501-01-XX', 'Salaries and Wages', 8, NULL, 'Employee salaries and wages', 'system'),
    ('501-03-XX', 'Payroll Taxes', 8, NULL, 'Employer portion of payroll taxes', 'system'),
    
    -- Operating Expenses
    ('501-10-XX', 'Rent Expense', 8, NULL, 'Office and facility rent', 'system'),
    ('501-11-XX', 'Utilities Expense', 8, NULL, 'Electricity, water, gas, internet, etc.', 'system'),
   
    -- Financial Expenses
    ('501-60-XX', 'Interest Expense', 8, NULL, 'Interest paid on borrowings', 'system'),
    ('501-61-XX', 'Bad Debt Expense', 8, NULL, 'Losses from uncollectible receivables', 'system'),
    ('501-62-XX', 'Loan Loss Provision', 8, NULL, 'Provision for potential loan losses', 'system'),
    
    -- Other Expenses
    ('501-93-XX', 'Miscellaneous Expenses', 8, NULL, 'Other sundry expenses', 'system');
