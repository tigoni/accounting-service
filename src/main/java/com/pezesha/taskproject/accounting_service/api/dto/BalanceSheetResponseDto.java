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
public class BalanceSheetResponseDto {
  private LocalDateTime asOfDate;
  private BalanceSheetSectionDto assets;
  private BalanceSheetSectionDto liabilities;
  private BalanceSheetSectionDto equity;
  private BigDecimal totalAssets;
  private BigDecimal totalLiabilitiesAndEquity;
  private Boolean isBalanced;
}

