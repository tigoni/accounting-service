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
public class BalanceSheetItemDto {
  private String accountNumber;
  private String accountName;
  private BigDecimal balance;
}

