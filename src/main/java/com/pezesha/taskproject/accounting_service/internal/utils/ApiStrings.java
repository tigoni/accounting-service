package com.pezesha.taskproject.accounting_service.internal.utils;

public class ApiStrings {

  // prevent instantiation
  private ApiStrings() {
    throw new IllegalStateException("Cannot instatiate this utility class");
  }

  // Routes
  public static final String ROOT_PATH = "/api/bookeeper/v1/";

  // Transaction uris
  public static final String CREATE_TRANSACTION = ROOT_PATH + "transaction";

  // Report uris
  public static final String URI_REPORTS_INCOME_STATEMENT =
      ROOT_PATH + "reports/income-statement/{groupUuid}";
  public static final String URI_REPORTS_BALANCE_SHEET =
      ROOT_PATH + "{groupUuid}/reports/balance-sheet";
  public static final String URI_REPORTS_TRIAL_BALANCE =
      ROOT_PATH + "{groupUuid}/reports/trial-balance";
  // Error messages
  public static final String ERROR_ACCOUNT_NOT_FOUND = "Account not found";
  public static final String ERROR_CREATE_JOURNAL_ENTRY = "Error creating journal entry";
  public static final String ERROR_JOURNAL_ENTRY_NOT_FOUND = "Journal entry not found";
  public static final String ERROR_CREATE_LEDGER_ENTRY = "Error creating ledger entry";
  public static final String ERROR_GROUP_EXISTS =
      "Group with the email or uuid already registered for this service";
}
