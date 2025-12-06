package com.pezesha.taskproject.accounting_service.api.exception;

import jakarta.persistence.PersistenceException;

public class TransactionSaveException extends PersistenceException {
  public TransactionSaveException(String message) {
    super(message);
  }
}
