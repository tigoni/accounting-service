package com.pezesha.taskproject.accounting_service.api.controller;

import com.pezesha.taskproject.accounting_service.api.dto.TransactionHistoryResponseDto;
import com.pezesha.taskproject.accounting_service.internal.service.TransactionHistoryService;
import com.pezesha.taskproject.accounting_service.internal.utils.ApiStrings;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TransactionHistoryController {

  private final TransactionHistoryService transactionHistoryService;

  public TransactionHistoryController(TransactionHistoryService transactionHistoryService) {
    this.transactionHistoryService = transactionHistoryService;
  }

  @GetMapping(ApiStrings.ACCOUNT_TRANSACTION_HISTORY)
  public ResponseEntity<ApiResponse<TransactionHistoryResponseDto>> getTransactionHistory(
      @PathVariable String accountName,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    try {
      log.debug("Getting transaction history for account: {} with filters - startDate: {}, endDate: {}, page: {}, size: {}",
          accountName, startDate, endDate, page, size);
      TransactionHistoryResponseDto history = transactionHistoryService.getTransactionHistory(
          accountName, startDate, endDate, page, size);
      return new ResponseEntity<>(
          new ApiResponse<>(true, "Transaction history retrieved successfully", history, null),
          HttpStatus.OK);
    } catch (EntityNotFoundException e) {
      log.error("Error retrieving transaction history", e);
      return new ResponseEntity<>(
          new ApiResponse<>(false, "Account not found", null, List.of(e.getMessage())),
          HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      log.error("Error retrieving transaction history", e);
      return new ResponseEntity<>(
          new ApiResponse<>(false, "Error retrieving transaction history", null, List.of(e.getMessage())),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}

