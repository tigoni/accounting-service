package com.pezesha.taskproject.accounting_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
  // @NotBlank(message = "Currency symbol is required")
  // private String currencySymbol;
}
