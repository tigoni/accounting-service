package com.pezesha.taskproject.accounting_service.internal.service;

import com.pezesha.taskproject.accounting_service.api.dto.BalanceSheetItemDto;
import com.pezesha.taskproject.accounting_service.api.dto.BalanceSheetResponseDto;
import com.pezesha.taskproject.accounting_service.api.dto.BalanceSheetSectionDto;
import com.pezesha.taskproject.accounting_service.internal.entity.Account;
import com.pezesha.taskproject.accounting_service.internal.entity.AccountType;
import com.pezesha.taskproject.accounting_service.internal.repository.AccountRepository;
import com.pezesha.taskproject.accounting_service.internal.repository.TransactionLineRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceSheetService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private TransactionLineRepository transactionLineRepository;

  public BalanceSheetResponseDto getBalanceSheet(LocalDateTime asOfDate) {
    List<Account> allAccounts = accountRepository.findAll();
    Map<Long, AccountTotals> accountTotalsMap = buildAccountTotalsMap();
    
    BalanceSheetSectionDto assets = buildAssetsSection(allAccounts, accountTotalsMap);
    BalanceSheetSectionDto liabilities = buildLiabilitiesSection(allAccounts, accountTotalsMap);
    BalanceSheetSectionDto equity = buildEquitySection(allAccounts, accountTotalsMap);
    
    BigDecimal totalAssets = assets.getTotal();
    BigDecimal totalLiabilitiesAndEquity = liabilities.getTotal().add(equity.getTotal());
    boolean isBalanced = totalAssets.compareTo(totalLiabilitiesAndEquity) == 0;

    return BalanceSheetResponseDto.builder()
        .asOfDate(asOfDate != null ? asOfDate : LocalDateTime.now())
        .assets(assets)
        .liabilities(liabilities)
        .equity(equity)
        .totalAssets(totalAssets)
        .totalLiabilitiesAndEquity(totalLiabilitiesAndEquity)
        .isBalanced(isBalanced)
        .build();
  }

  private Map<Long, AccountTotals> buildAccountTotalsMap() {
    List<Object[]> accountBalances = transactionLineRepository.getAccountBalances();
    Map<Long, AccountTotals> totalsMap = new HashMap<>();
    
    for (Object[] balance : accountBalances) {
      Long accountId = ((Number) balance[0]).longValue();
      BigDecimal debitTotal = (BigDecimal) balance[1];
      BigDecimal creditTotal = (BigDecimal) balance[2];
      totalsMap.put(accountId, new AccountTotals(debitTotal, creditTotal));
    }
    
    return totalsMap;
  }

  private BalanceSheetSectionDto buildAssetsSection(List<Account> allAccounts, Map<Long, AccountTotals> accountTotalsMap) {
    List<BalanceSheetItemDto> assetAccounts = new ArrayList<>();
    
    for (Account account : allAccounts) {
      if (account.getAccountType().isAsset()) {
        BigDecimal balance = calculateAccountBalance(account, accountTotalsMap);
        if (balance.compareTo(BigDecimal.ZERO) != 0) {
          assetAccounts.add(BalanceSheetItemDto.builder()
              .accountNumber(account.getAccountNumber())
              .accountName(account.getAccountName())
              .balance(balance)
              .build());
        }
      }
    }
    
    BigDecimal total = assetAccounts.stream()
        .map(BalanceSheetItemDto::getBalance)
        .collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add));
    
    return BalanceSheetSectionDto.builder()
        .sectionName("Assets")
        .accounts(assetAccounts)
        .total(total)
        .build();
  }

  private BalanceSheetSectionDto buildLiabilitiesSection(List<Account> allAccounts, Map<Long, AccountTotals> accountTotalsMap) {
    List<BalanceSheetItemDto> liabilityAccounts = new ArrayList<>();
    
    for (Account account : allAccounts) {
      if (account.getAccountType().isLiability()) {
        BigDecimal balance = calculateAccountBalance(account, accountTotalsMap);
        if (balance.compareTo(BigDecimal.ZERO) != 0) {
          liabilityAccounts.add(BalanceSheetItemDto.builder()
              .accountNumber(account.getAccountNumber())
              .accountName(account.getAccountName())
              .balance(balance)
              .build());
        }
      }
    }
    
    BigDecimal total = liabilityAccounts.stream()
        .map(BalanceSheetItemDto::getBalance)
        .collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add));
    
    return BalanceSheetSectionDto.builder()
        .sectionName("Liabilities")
        .accounts(liabilityAccounts)
        .total(total)
        .build();
  }

  private BalanceSheetSectionDto buildEquitySection(List<Account> allAccounts, Map<Long, AccountTotals> accountTotalsMap) {
    List<BalanceSheetItemDto> equityAccounts = new ArrayList<>();
    
    for (Account account : allAccounts) {
      if (account.getAccountType().isEquity()) {
        BigDecimal balance = calculateAccountBalance(account, accountTotalsMap);
        if (balance.compareTo(BigDecimal.ZERO) != 0) {
          equityAccounts.add(BalanceSheetItemDto.builder()
              .accountNumber(account.getAccountNumber())
              .accountName(account.getAccountName())
              .balance(balance)
              .build());
        }
      }
    }
    
    BigDecimal total = equityAccounts.stream()
        .map(BalanceSheetItemDto::getBalance)
        .collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add));
    
    return BalanceSheetSectionDto.builder()
        .sectionName("Equity")
        .accounts(equityAccounts)
        .total(total)
        .build();
  }

  private BigDecimal calculateAccountBalance(Account account, Map<Long, AccountTotals> accountTotalsMap) {
    AccountTotals totals = accountTotalsMap.getOrDefault(
        account.getId(),
        new AccountTotals(BigDecimal.ZERO, BigDecimal.ZERO)
    );
    
    AccountType accountType = account.getAccountType();
    
    if (accountType.isAsset()) {
      return totals.debitTotal.subtract(totals.creditTotal);
    } else if (accountType.isLiability() || accountType.isEquity()) {
      return totals.creditTotal.subtract(totals.debitTotal);
    }
    
    return BigDecimal.ZERO;
  }

  private static class AccountTotals {
    final BigDecimal debitTotal;
    final BigDecimal creditTotal;
    
    AccountTotals(BigDecimal debitTotal, BigDecimal creditTotal) {
      this.debitTotal = debitTotal;
      this.creditTotal = creditTotal;
    }
  }
}

