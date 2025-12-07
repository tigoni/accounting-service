package com.pezesha.taskproject.accounting_service.api.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionHistoryResponseDto {
  private String accountName;
  private String accountNumber;
  private List<TransactionHistoryItemDto> transactions;
  private int currentPage;
  private int totalPages;
  private long totalElements;
  private int pageSize;
}

