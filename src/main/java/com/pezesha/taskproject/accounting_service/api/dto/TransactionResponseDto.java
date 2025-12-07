package com.pezesha.taskproject.accounting_service.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
  private LocalDateTime date;
  private String description;
  private String reference;
  private String accountUuid;
  private String accountName;
  private BigDecimal balance;
  private String balanceType;
  private String balanceDate;
  private java.util.List<TransactionLineDto> lines;
}
