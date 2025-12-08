package com.pezesha.taskproject.accounting_service.api.controller;

import com.pezesha.taskproject.accounting_service.api.dto.BalanceSheetResponseDto;
import com.pezesha.taskproject.accounting_service.internal.service.BalanceSheetService;
import com.pezesha.taskproject.accounting_service.internal.utils.ApiStrings;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BalanceSheetController {

  private final BalanceSheetService balanceSheetService;

  public BalanceSheetController(BalanceSheetService balanceSheetService) {
    this.balanceSheetService = balanceSheetService;
  }

  @GetMapping(ApiStrings.BALANCE_SHEET)
  public ResponseEntity<ApiResponse<BalanceSheetResponseDto>> getBalanceSheet(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime asOfDate) {
    try {
      log.debug("Generating balance sheet as of date: {}", asOfDate);
      BalanceSheetResponseDto balanceSheet = balanceSheetService.getBalanceSheet(asOfDate);
      return new ResponseEntity<>(
          new ApiResponse<>(true, "Balance sheet retrieved successfully", balanceSheet, null),
          HttpStatus.OK);
    } catch (Exception e) {
      log.error("Error generating balance sheet", e);
      return new ResponseEntity<>(
          new ApiResponse<>(false, "Error generating balance sheet", null, List.of(e.getMessage())),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}

