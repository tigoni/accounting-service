package com.pezesha.taskproject.accounting_service.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountBalanceResponseDto {
  private String accountId;
  private String accountName;
  private String accountNumber;
  private BigDecimal debitTotal;
  private BigDecimal creditTotal;
  private BigDecimal balance;
  private LocalDate asOfDate;
  private Boolean isCurrentBalance;
}


