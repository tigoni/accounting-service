package com.pezesha.taskproject.accounting_service.internal.utils;

public class ApiStrings {

  // prevent instantiation
  private ApiStrings() {
    throw new IllegalStateException("Cannot instatiate this utility class");
  }

  // Routes
  public static final String ROOT_PATH = "/api/v1/acc-service";

  // Transaction uris
  public static final String LOAN_DISBURSEMENT = ROOT_PATH + "/loans/disbursement";
  public static final String LOAN_REVERSAL = ROOT_PATH + "/loans/{uuid}/reversal";
  public static final String LOAN_REPAYMENT = ROOT_PATH + "/loans/{uuid}/repayment";
  public static final String LOAN_WRITEOFF = ROOT_PATH + "/loans/{uuid}/writeoff";

  // Report uris
  public static final String ACCOUNT_BALANCE = ROOT_PATH + "/accounts/{accountName}/balance";
  public static final String ACCOUNT_TRANSACTION_HISTORY = ROOT_PATH + "/accounts/{accountName}/transactions";
  public static final String TRIAL_BALANCE = ROOT_PATH + "/reports/trial-balance";
  public static final String BALANCE_SHEET = ROOT_PATH + "/reports/balance-sheet";

  // Error messages
  public static final String ERROR_ACCOUNT_NOT_FOUND = "Account not found";
  public static final String ERROR_CREATE_JOURNAL_ENTRY = "Error creating journal entry";
  public static final String ERROR_JOURNAL_ENTRY_NOT_FOUND = "Journal entry not found";
  public static final String ERROR_CREATE_LEDGER_ENTRY = "Error creating ledger entry";
}
