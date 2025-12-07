package com.pezesha.taskproject.accounting_service.api.controller;

import com.pezesha.taskproject.accounting_service.api.dto.TrialBalanceResponseDto;
import com.pezesha.taskproject.accounting_service.internal.service.TrialBalanceService;
import com.pezesha.taskproject.accounting_service.internal.utils.ApiStrings;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TrialBalanceController {

  private final TrialBalanceService trialBalanceService;

  public TrialBalanceController(TrialBalanceService trialBalanceService) {
    this.trialBalanceService = trialBalanceService;
  }

  @GetMapping(ApiStrings.TRIAL_BALANCE)
  public ResponseEntity<ApiResponse<TrialBalanceResponseDto>> getTrialBalance() {
    try {
      log.debug("Generating trial balance");
      TrialBalanceResponseDto trialBalance = trialBalanceService.getTrialBalance();
      return new ResponseEntity<>(
          new ApiResponse<>(true, "Trial balance retrieved successfully", trialBalance, null),
          HttpStatus.OK);
    } catch (Exception e) {
      log.error("Error generating trial balance", e);
      return new ResponseEntity<>(
          new ApiResponse<>(false, "Error generating trial balance", null, List.of(e.getMessage())),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}

