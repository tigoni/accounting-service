package com.pezesha.taskproject.accounting_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionRequestDto {
  @NotBlank(message = "Idempotency key is required")  
  private String idempotencyKey; 
  private String description;
  @NotBlank(message = "Transaction reference is required")
  private String reference;
  @NotEmpty(message = "Transaction lines are required")
  private List<TransactionLineDto> transactionLines;

  @NotNull(message = "Transaction date is required")
  @PastOrPresent(message = "Transaction date cannot be in the future")
  private LocalDateTime transactionDate;

  // @NotBlank(message = "Currency symbol is required")
  // private String currencySymbol;
}
