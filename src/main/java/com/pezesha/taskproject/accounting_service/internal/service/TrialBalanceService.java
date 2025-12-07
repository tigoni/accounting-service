package com.pezesha.taskproject.accounting_service.internal.service;

import com.pezesha.taskproject.accounting_service.api.dto.TrialBalanceGroupDto;
import com.pezesha.taskproject.accounting_service.api.dto.TrialBalanceItemDto;
import com.pezesha.taskproject.accounting_service.api.dto.TrialBalanceResponseDto;
import com.pezesha.taskproject.accounting_service.internal.entity.Account;
import com.pezesha.taskproject.accounting_service.internal.entity.AccountType;
import com.pezesha.taskproject.accounting_service.internal.repository.AccountRepository;
import com.pezesha.taskproject.accounting_service.internal.repository.TransactionLineRepository;
import java.math.BigDecimal;
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
public class TrialBalanceService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private TransactionLineRepository transactionLineRepository;

  public TrialBalanceResponseDto getTrialBalance() {
    List<Account> allAccounts = accountRepository.findAll();
    Map<Long, AccountTotals> accountTotalsMap = buildAccountTotalsMap();
    
    Map<AccountType, List<TrialBalanceItemDto>> groupedAccounts = buildGroupedAccounts(allAccounts, accountTotalsMap);
    List<TrialBalanceGroupDto> groups = buildGroups(groupedAccounts);
    
    BigDecimal totalDebits = calculateTotalDebits(groups);
    BigDecimal totalCredits = calculateTotalCredits(groups);
    boolean isBalanced = totalDebits.compareTo(totalCredits) == 0;

    return TrialBalanceResponseDto.builder()
        .groups(groups)
        .totalDebits(totalDebits)
        .totalCredits(totalCredits)
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

  private Map<AccountType, List<TrialBalanceItemDto>> buildGroupedAccounts(
      List<Account> allAccounts, 
      Map<Long, AccountTotals> accountTotalsMap) {
    
    Map<AccountType, List<TrialBalanceItemDto>> groupedAccounts = new HashMap<>();
    
    for (Account account : allAccounts) {
      AccountTotals totals = accountTotalsMap.getOrDefault(
          account.getId(), 
          new AccountTotals(BigDecimal.ZERO, BigDecimal.ZERO)
      );
      
      TrialBalanceItemDto item = calculateAccountBalance(account, totals);
      if (item != null) {
        groupedAccounts.computeIfAbsent(account.getAccountType(), k -> new ArrayList<>()).add(item);
      }
    }
    
    return groupedAccounts;
  }

  private TrialBalanceItemDto calculateAccountBalance(Account account, AccountTotals totals) {
    BalancePair balancePair = calculateDebitCreditBalances(account.getAccountType(), totals);
    
    if (balancePair.hasNoBalance()) {
      return null;
    }
    
    return TrialBalanceItemDto.builder()
        .accountNumber(account.getAccountNumber())
        .accountName(account.getAccountName())
        .debitBalance(balancePair.debitBalance)
        .creditBalance(balancePair.creditBalance)
        .build();
  }

  private BalancePair calculateDebitCreditBalances(AccountType accountType, AccountTotals totals) {
    boolean isDebitNormalAccount = accountType.isAsset() || accountType.isExpense();
    
    BigDecimal netBalance = isDebitNormalAccount
        ? totals.debitTotal.subtract(totals.creditTotal)
        : totals.creditTotal.subtract(totals.debitTotal);
    
    BigDecimal debitBalance = BigDecimal.ZERO;
    BigDecimal creditBalance = BigDecimal.ZERO;
    
    if (netBalance.compareTo(BigDecimal.ZERO) > 0) {
      if (isDebitNormalAccount) {
        debitBalance = netBalance;
      } else {
        creditBalance = netBalance;
      }
    } else if (netBalance.compareTo(BigDecimal.ZERO) < 0) {
      if (isDebitNormalAccount) {
        creditBalance = netBalance.abs();
      } else {
        debitBalance = netBalance.abs();
      }
    }
    
    return new BalancePair(debitBalance, creditBalance);
  }

  private List<TrialBalanceGroupDto> buildGroups(Map<AccountType, List<TrialBalanceItemDto>> groupedAccounts) {
    List<TrialBalanceGroupDto> groups = new ArrayList<>();
    
    for (Map.Entry<AccountType, List<TrialBalanceItemDto>> entry : groupedAccounts.entrySet()) {
      AccountType accountType = entry.getKey();
      List<TrialBalanceItemDto> items = entry.getValue();
      
      BigDecimal groupDebits = items.stream()
          .map(TrialBalanceItemDto::getDebitBalance)
          .collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add));
      
      BigDecimal groupCredits = items.stream()
          .map(TrialBalanceItemDto::getCreditBalance)
          .collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add));
      
      groups.add(TrialBalanceGroupDto.builder()
          .accountTypeName(accountType.getAccountName())
          .accountTypeId(accountType.getId().intValue())
          .accounts(items)
          .totalDebits(groupDebits)
          .totalCredits(groupCredits)
          .build());
    }
    
    groups.sort((a, b) -> Integer.compare(a.getAccountTypeId(), b.getAccountTypeId()));
    return groups;
  }

  private BigDecimal calculateTotalDebits(List<TrialBalanceGroupDto> groups) {
    return groups.stream()
        .map(TrialBalanceGroupDto::getTotalDebits)
        .collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add));
  }

  private BigDecimal calculateTotalCredits(List<TrialBalanceGroupDto> groups) {
    return groups.stream()
        .map(TrialBalanceGroupDto::getTotalCredits)
        .collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add));
  }

  private static class AccountTotals {
    final BigDecimal debitTotal;
    final BigDecimal creditTotal;
    
    AccountTotals(BigDecimal debitTotal, BigDecimal creditTotal) {
      this.debitTotal = debitTotal;
      this.creditTotal = creditTotal;
    }
  }

  private static class BalancePair {
    final BigDecimal debitBalance;
    final BigDecimal creditBalance;
    
    BalancePair(BigDecimal debitBalance, BigDecimal creditBalance) {
      this.debitBalance = debitBalance;
      this.creditBalance = creditBalance;
    }
    
    boolean hasNoBalance() {
      return debitBalance.compareTo(BigDecimal.ZERO) == 0 
          && creditBalance.compareTo(BigDecimal.ZERO) == 0;
    }
  }
}

