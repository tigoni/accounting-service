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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
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

  /**
   * Create a transaction
   * @param transactionDto
   * @return TransactionResponseDto
   * @throws TransactionSaveException
   */
  public TransactionResponseDto createTransaction(TransactionRequestDto transactionDto) throws TransactionSaveException {
    Optional<Transaction> existingTransaction = repository.findByIdempotencyKey(transactionDto.getIdempotencyKey());
    if (existingTransaction.isPresent()) {
      log.info("Transaction with idempotency key {} already exists. Returning existing transaction.",
          transactionDto.getIdempotencyKey());
      Transaction result = existingTransaction.get();
      return mapToResponseDto(result);

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
        .idempotencyKey(transactionDto.getIdempotencyKey())
        .lines(lines)
        .description(transactionDto.getDescription())
        .build();
    lines.forEach(line -> line.setTransaction(newTransaction));
    try {
      Transaction result = repository.save(newTransaction);
      return mapToResponseDto(result);
    } catch (OptimisticLockingFailureException e) {
      log.error("Optimistic lock exception", e);
      throw new TransactionSaveException("Transaction has been modified by another user. Please try again.");
    } catch (Exception e) {
      log.error("Error saving transaction", e);
      throw new TransactionSaveException("Failed to save transaction");
    }
  }


  /**
   * Reverse a transaction
   * If the transaction has already been reversed, return the existing reversal transaction
   * If the transaction has not been reversed, create a new reversal transaction
   * @param idempotencyKey
   * @return TransactionResponseDto
   * @throws TransactionSaveException
   * @throws EntityNotFoundException
   */ 
  public TransactionResponseDto reverseTransaction(String idempotencyKey) {
    Transaction originalTransaction = repository.findByIdempotencyKey(idempotencyKey)
        .orElseThrow(() -> new EntityNotFoundException("Transaction not found: " + idempotencyKey));

    if (originalTransaction.getReversedAt() != null) {
      throw new TransactionSaveException("Transaction already reversed");
    }

    String reversalIdempotencyKey = idempotencyKey + "_REVERSED";
    Optional<Transaction> existingReversal = repository.findByIdempotencyKey(reversalIdempotencyKey);
    if (existingReversal.isPresent()) {
      return mapToResponseDto(existingReversal.get());
    }

    List<TransactionLine> reversalLines = originalTransaction.getLines().stream()
        .map(originalLine -> TransactionLine.builder()
            .account(originalLine.getAccount())
            .debitAmount(originalLine.getCreditAmount())
            .creditAmount(originalLine.getDebitAmount())
            .build())
        .collect(Collectors.toList());

    Transaction reversalTransaction = Transaction.builder()
        .idempotencyKey(reversalIdempotencyKey)
        .description("Reversal of: " + originalTransaction.getDescription())
        .reversedTransaction(originalTransaction)
        .lines(reversalLines)
        .build();

    reversalLines.forEach(line -> line.setTransaction(reversalTransaction));

    try {
      Transaction savedReversal = repository.save(reversalTransaction);
      originalTransaction.setReversedAt(LocalDateTime.now());
      originalTransaction.setReversedTransaction(savedReversal);
      repository.save(originalTransaction);
      return mapToResponseDto(savedReversal);
    } catch (OptimisticLockingFailureException e) {
      log.error("Optimistic lock exception", e);
      throw new TransactionSaveException("Transaction has been modified by another user. Please try again.");
    } catch (Exception e) {
      log.error("Error saving transaction", e);
      throw new TransactionSaveException("Failed to save transaction");
    }

  }

  private TransactionResponseDto mapToResponseDto(Transaction transaction) {
    return TransactionResponseDto.builder()
        .idempotencyKey(transaction.getIdempotencyKey())
        .description(transaction.getDescription())
        .date(transaction.getCreatedAt())
        .lines(transaction.getLines().stream()
            .map(line -> TransactionLineDto.builder()
                .creditAmount(line.getCreditAmount())
                .debitAmount(line.getDebitAmount())
                .accountId(line.getAccount().getAccountName())
                .build())
            .collect(Collectors.toList()))
        .build();
  }

  private Account getAccountByName(String accountName) {
    System.out.println("Fetching account for name: " + accountName);
    String normalizedAccountName = accountName.replace('-', ' ');
    Account account = accountRepository.findByAccountNameIgnoreCase(normalizedAccountName);
    if (account == null) {
      throw new EntityNotFoundException("Account not found: " + accountName);
    }
    return account;
  }
}
