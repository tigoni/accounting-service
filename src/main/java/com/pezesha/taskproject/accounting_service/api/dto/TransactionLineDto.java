package com.pezesha.taskproject.accounting_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionLineDto {

  @Size(min = 3, max = 30, message = "Code must be min 3 chars and max 30 chars")
  @NotBlank(message = "Credit account id is required")
  private String accountId;

  @NotNull(message = "Either debit amount or credit amount must be provided")
  private BigDecimal debitAmount;
  private BigDecimal creditAmount;
}
