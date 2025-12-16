package com.pezesha.taskproject.accounting_service.internal.service;

import com.pezesha.taskproject.accounting_service.api.dto.TransactionHistoryItemDto;
import com.pezesha.taskproject.accounting_service.api.dto.TransactionHistoryResponseDto;
import com.pezesha.taskproject.accounting_service.internal.entity.Account;
import com.pezesha.taskproject.accounting_service.internal.entity.AccountType;
import com.pezesha.taskproject.accounting_service.internal.entity.TransactionLine;
import com.pezesha.taskproject.accounting_service.internal.repository.AccountRepository;
import com.pezesha.taskproject.accounting_service.internal.repository.TransactionLineRepository;
import com.pezesha.taskproject.accounting_service.internal.utils.Utils;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionHistoryService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private TransactionLineRepository transactionLineRepository;

  public TransactionHistoryResponseDto getTransactionHistory(
      String accountName,
      LocalDateTime startDate,
      LocalDateTime endDate,
      int page,
      int size) {
    
    String normalizedAccountName = Utils.normalizeAccountName(accountName);
    Account account = accountRepository.findByNormalizedName(normalizedAccountName);
    if (account == null) {
      throw new EntityNotFoundException("Account not found: " + accountName);
    }

    LocalDate effectiveStartDate = startDate != null ? startDate.toLocalDate() : LocalDate.of(1970, 1, 1);
    LocalDate effectiveEndDate = endDate != null ? endDate.toLocalDate() : LocalDate.of(9999, 12, 31);
    
    Pageable pageable = PageRequest.of(page, size);
    Page<TransactionLine> transactionLinesPage = transactionLineRepository.findByAccountIdWithDateRange(
        account.getId(), effectiveStartDate.atStartOfDay(), effectiveEndDate.atTime(23, 59, 59), pageable);
        
    BigDecimal openingBalance = calculateOpeningBalance(account, effectiveStartDate);
    List<TransactionHistoryItemDto> transactions = buildTransactionHistory(
        transactionLinesPage.getContent(), account.getAccountType(), openingBalance);

    return TransactionHistoryResponseDto.builder()
        .accountName(account.getAccountName())
        .accountNumber(account.getAccountNumber())
        .transactions(transactions)
        .currentPage(transactionLinesPage.getNumber())
        .totalPages(transactionLinesPage.getTotalPages())
        .totalElements(transactionLinesPage.getTotalElements())
        .pageSize(transactionLinesPage.getSize())
        .build();
  }

  private BigDecimal calculateOpeningBalance(Account account, LocalDate startDate) {
    if (startDate == null) {
      return BigDecimal.ZERO;
    }

    BigDecimal debitTotal = transactionLineRepository.sumDebitAmountByAccountIdAndDate(
        account.getId(), startDate.atStartOfDay());
    BigDecimal creditTotal = transactionLineRepository.sumCreditAmountByAccountIdAndDate(
        account.getId(), startDate.atStartOfDay());

    return calculateBalance(account.getAccountType(), debitTotal, creditTotal);
  }

  private List<TransactionHistoryItemDto> buildTransactionHistory(
      List<TransactionLine> lines,
      AccountType accountType,
      BigDecimal openingBalance) {
    
    java.util.concurrent.atomic.AtomicReference<BigDecimal> runningBalance = 
        new java.util.concurrent.atomic.AtomicReference<>(openingBalance);
    
    return lines.stream()
        .map(line -> {
          BigDecimal balanceChange = calculateBalanceChange(accountType, line);
          BigDecimal currentBalance = runningBalance.updateAndGet(bal -> bal.add(balanceChange));
          
          return TransactionHistoryItemDto.builder()
              .transactionId(line.getTransaction().getId().toString())
              .uuid(line.getTransaction().getUuid())
              .idempotencyKey(line.getTransaction().getIdempotencyKey())
              .date(line.getCreatedAt())
              .description(line.getTransaction().getDescription())
              .reference(line.getTransaction().getIdempotencyKey())
              .debitAmount(line.getDebitAmount())
              .creditAmount(line.getCreditAmount())
              .runningBalance(currentBalance)
              .build();
        })
        .collect(Collectors.toList());
  }

  private BigDecimal calculateBalanceChange(AccountType accountType, TransactionLine line) {
    if (accountType.isAsset() || accountType.isExpense()) {
      return line.getDebitAmount().subtract(line.getCreditAmount());
    } else {
      return line.getCreditAmount().subtract(line.getDebitAmount());
    }
  }

  private BigDecimal calculateBalance(AccountType accountType, BigDecimal debitTotal, BigDecimal creditTotal) {
    if (accountType.isAsset() || accountType.isExpense()) {
      return debitTotal.subtract(creditTotal);
    } else {
      return creditTotal.subtract(debitTotal);
    }
  }
}

