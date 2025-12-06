package com.pezesha.taskproject.accounting_service.internal.service;

import com.pezesha.taskproject.accounting_service.api.dto.TransactionLineDto;
import com.pezesha.taskproject.accounting_service.api.dto.TransactionRequestDto;
import com.pezesha.taskproject.accounting_service.api.dto.TransactionResponseDto;
import com.pezesha.taskproject.accounting_service.api.exception.TransactionSaveException;
import com.pezesha.taskproject.accounting_service.internal.entity.Transaction;
import com.pezesha.taskproject.accounting_service.internal.entity.TransactionLine;
import com.pezesha.taskproject.accounting_service.internal.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionService extends BasicService<Transaction, TransactionRepository> {

  public TransactionService(
      TransactionRepository transactionRepository) {
    super(transactionRepository);
  }

  public TransactionResponseDto createTransaction(TransactionRequestDto transactionDto) {
    //build transaction entity from dto
    List<TransactionLine> lines = transactionDto.getTransactionLines().stream()
        .map(lineDto -> TransactionLine.builder()
            .creditAmount(lineDto.getCreditAmount())
            .debitAmount(lineDto.getDebitAmount())
            .build())
        .collect(Collectors.toList());

    Transaction newTransaction = Transaction.builder()
        .transactionDate(transactionDto.getTransactionDate())
        .transactionReference(transactionDto.getReference())
        .lines(lines)
        // .currency(transaction.getCurrency())
        .description(transactionDto.getDescription())
        .build();
    try {
      Transaction result  =repository.save(newTransaction);
      return TransactionResponseDto.builder()
          .accountUuid(result.getUuid())
          .date(result.getTransactionDate())
          .reference(result.getTransactionReference())
          .description(result.getDescription())
          .lines(null)
          // .lines(
          //     result.getLines().stream()
          //         .map(line -> TransactionLineDto.builder()
          //             .creditAmount(line.getCreditAmount())
          //             .debitAmount(line.getDebitAmount())
          //             .build())
          //         .collect(Collectors.toList()))
          .build();
    } catch (Exception e) {
      log.error("Error saving transaction", e);
      throw new TransactionSaveException("Failed to save transaction");
    }
  }

}
