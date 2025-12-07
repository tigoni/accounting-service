package com.pezesha.taskproject.accounting_service.internal.service;

import com.pezesha.taskproject.accounting_service.api.dto.TransactionRequestDto;
import com.pezesha.taskproject.accounting_service.api.dto.TransactionResponseDto;
import com.pezesha.taskproject.accounting_service.api.dto.TransactionLineDto;
import com.pezesha.taskproject.accounting_service.api.exception.TransactionSaveException;
import com.pezesha.taskproject.accounting_service.internal.entity.Account;
import com.pezesha.taskproject.accounting_service.internal.entity.Transaction;
import com.pezesha.taskproject.accounting_service.internal.entity.TransactionLine;
import com.pezesha.taskproject.accounting_service.internal.repository.AccountRepository;
import com.pezesha.taskproject.accounting_service.internal.repository.TransactionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionService extends BasicService<Transaction, TransactionRepository> {

  @Autowired
  AccountRepository accountRepository;

  public TransactionService(
      TransactionRepository transactionRepository) {
    super(transactionRepository);
  }

  public TransactionResponseDto createTransaction(TransactionRequestDto transactionDto) {
    Optional<Transaction> existingTransaction = repository.findByIdempotencyKey(transactionDto.getIdempotencyKey());
    if (existingTransaction.isPresent()) {
      log.info("Transaction with idempotency key {} already exists. Returning existing transaction.",
          transactionDto.getIdempotencyKey());
      Transaction result = existingTransaction.get();
      return TransactionResponseDto.builder()
          .lines(
          result.getLines().stream()
          .map(line -> TransactionLineDto.builder()
              .creditAmount(line.getCreditAmount())
              .debitAmount(line.getDebitAmount())
              .accountId(line.getAccount().getAccountName())
              .build())
          .collect(Collectors.toList()))
          .build();
    }

    // build transaction entity from dto
    List<TransactionLine> lines = transactionDto.getTransactionLines().stream()
        .map(lineDto -> TransactionLine.builder()
            .creditAmount(lineDto.getCreditAmount())
            .debitAmount(lineDto.getDebitAmount())
            .account(getAccountByName(lineDto.getAccountId()))
            .build())
        .collect(Collectors.toList());

    Transaction newTransaction = Transaction.builder()
        .lines(lines)
        .description(transactionDto.getDescription())
        .build();
    lines.forEach(line -> line.setTransaction(newTransaction));
    try {
      Transaction result = repository.save(newTransaction);
      return TransactionResponseDto.builder()
          .description(result.getDescription())
          .lines(
          result.getLines().stream()
          .map(line -> TransactionLineDto.builder()
          .creditAmount(line.getCreditAmount())
          .debitAmount(line.getDebitAmount())
          .build())
          .collect(Collectors.toList()))
          .build();
    } catch (Exception e) {
      log.error("Error saving transaction", e);
      throw new TransactionSaveException("Failed to save transaction");
    }
  }

  // method to get account from account name
  private Account getAccountByName(String accountName) {
    System.out.println("Fetching account for name: " + accountName);
    com.pezesha.taskproject.accounting_service.internal.entity.Account account = accountRepository
        .findByAccountName(accountName);
    if (account == null) {
      throw new EntityNotFoundException("Account not found: " + accountName);
    }
    return account;
  }
}
