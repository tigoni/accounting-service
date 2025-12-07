package com.pezesha.taskproject.accounting_service.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionHistoryItemDto {
  private String transactionId;
  private String idempotencyKey;
  private LocalDateTime date;
  private String description;
  private String reference;
  private BigDecimal debitAmount;
  private BigDecimal creditAmount;
  private BigDecimal runningBalance;
}

