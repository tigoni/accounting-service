package com.pezesha.taskproject.accounting_service.internal.service;

import com.pezesha.taskproject.accounting_service.api.dto.AccountBalanceResponseDto;
import com.pezesha.taskproject.accounting_service.internal.entity.Account;
import com.pezesha.taskproject.accounting_service.internal.entity.AccountType;
import com.pezesha.taskproject.accounting_service.internal.repository.AccountRepository;
import com.pezesha.taskproject.accounting_service.internal.repository.TransactionLineRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountBalanceService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private TransactionLineRepository transactionLineRepository;

  public AccountBalanceResponseDto getAccountBalance(String accountName, LocalDateTime asOfDate) {
    String normalizedAccountName = normalizeAccountName(accountName);
    Account account = accountRepository.findByAccountNameIgnoreCase(normalizedAccountName);
    if (account == null) {
      throw new EntityNotFoundException("Account not found: " + accountName);
    }

    BigDecimal debitTotal;
    BigDecimal creditTotal;

    if (asOfDate != null) {
      debitTotal = transactionLineRepository.sumDebitAmountByAccountIdAndDate(account.getId(), asOfDate);
      creditTotal = transactionLineRepository.sumCreditAmountByAccountIdAndDate(account.getId(), asOfDate);
    } else {
      debitTotal = transactionLineRepository.sumDebitAmountByAccountId(account.getId());
      creditTotal = transactionLineRepository.sumCreditAmountByAccountId(account.getId());
    }

    BigDecimal balance = calculateBalance(account.getAccountType(), debitTotal, creditTotal);

    return AccountBalanceResponseDto.builder()
        .accountId(account.getId().toString())
        .accountName(account.getAccountName())
        .accountNumber(account.getAccountNumber())
        .debitTotal(debitTotal)
        .creditTotal(creditTotal)
        .balance(balance)
        .asOfDate(asOfDate != null ? asOfDate : LocalDateTime.now())
        .isCurrentBalance(asOfDate == null)
        .build();
  }

  private BigDecimal calculateBalance(AccountType accountType, BigDecimal debitTotal, BigDecimal creditTotal) {
    if (accountType.isAsset() || accountType.isExpense()) {
      return debitTotal.subtract(creditTotal);
    } else {
      return creditTotal.subtract(debitTotal);
    }
  }

  private String normalizeAccountName(String accountName) {
    if (accountName == null) {
      return null;
    }
    return accountName.replace('-', ' ');
  }
}


