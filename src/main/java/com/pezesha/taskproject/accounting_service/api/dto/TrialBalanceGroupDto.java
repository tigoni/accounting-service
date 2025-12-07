package com.pezesha.taskproject.accounting_service.api.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrialBalanceGroupDto {
  private String accountTypeName;
  private Integer accountTypeId;
  private List<TrialBalanceItemDto> accounts;
  private BigDecimal totalDebits;
  private BigDecimal totalCredits;
}

