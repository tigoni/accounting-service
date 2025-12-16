package com.pezesha.taskproject.accounting_service.api.controller;

import com.pezesha.taskproject.accounting_service.api.dto.AccountBalanceResponseDto;
import com.pezesha.taskproject.accounting_service.internal.service.AccountBalanceService;
import com.pezesha.taskproject.accounting_service.internal.utils.ApiStrings;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
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
public class AccountBalanceController {

  private final AccountBalanceService accountBalanceService;

  public AccountBalanceController(AccountBalanceService accountBalanceService) {
    this.accountBalanceService = accountBalanceService;
  }

  @GetMapping(ApiStrings.ACCOUNT_BALANCE)
  public ResponseEntity<ApiResponse<AccountBalanceResponseDto>> getAccountBalance(
      @PathVariable String accountName,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
    try {
      log.debug("Getting account balance for account: {} as of date: {}", accountName, asOfDate);
      AccountBalanceResponseDto balance = accountBalanceService.getAccountBalance(accountName, asOfDate);
      return new ResponseEntity<>(
          new ApiResponse<>(true, "Account balance retrieved successfully", balance, null),
          HttpStatus.OK);
    } catch (EntityNotFoundException e) {
      log.error("Error retrieving account balance", e);
      return new ResponseEntity<>(
          new ApiResponse<>(false, "Account not found", null, List.of(e.getMessage())),
          HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      log.error("Error retrieving account balance", e);
      return new ResponseEntity<>(
          new ApiResponse<>(false, "Error retrieving account balance", null, List.of(e.getMessage())),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}


