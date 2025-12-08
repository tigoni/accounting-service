package com.pezesha.taskproject.accounting_service.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionResponseDto {
  private UUID uuid;
  private LocalDateTime date;
  private String idempotencyKey;
  private String description;
  private String accountName;
  private BigDecimal balance;
  private java.util.List<TransactionLineDto> lines;
}
