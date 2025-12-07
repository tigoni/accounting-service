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
public class TrialBalanceResponseDto {
  private List<TrialBalanceGroupDto> groups;
  private BigDecimal totalDebits;
  private BigDecimal totalCredits;
  private Boolean isBalanced;
}

