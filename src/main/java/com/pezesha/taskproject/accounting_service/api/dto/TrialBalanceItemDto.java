package com.pezesha.taskproject.accounting_service.api.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrialBalanceItemDto {
  private String accountNumber;
  private String accountName;
  private BigDecimal debitBalance;
  private BigDecimal creditBalance;
}

