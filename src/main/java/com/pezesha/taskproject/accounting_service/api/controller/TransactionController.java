package com.pezesha.taskproject.accounting_service.api.controller;

import com.pezesha.taskproject.accounting_service.internal.utils.ApiStrings;
import com.pezesha.taskproject.accounting_service.api.dto.TransactionRequestDto;
import com.pezesha.taskproject.accounting_service.api.dto.TransactionResponseDto;
import com.pezesha.taskproject.accounting_service.api.exception.TransactionSaveException;
import com.pezesha.taskproject.accounting_service.internal.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@Slf4j
public class TransactionController {

  private final TransactionService transactionService;

  public TransactionController(TransactionService transactionTagService) {
    this.transactionService = transactionTagService;
  }


  @PostMapping(ApiStrings.LOAN_DISBURSEMENT)
  public ResponseEntity<ApiResponse<TransactionResponseDto>> create(@RequestBody @Valid TransactionRequestDto transactionDto) {
    TransactionResponseDto transactionResponseDto = null;
    try{
      log.debug("Creating ransaction with accoounts and lines: {}", transactionDto.getTransactionLines().stream()
          .map(line -> line.getAccountId()).toList());
      transactionResponseDto = transactionService.createTransaction(transactionDto);
    } catch (EntityNotFoundException e) {
      log.error("Error creating transaction", e);
      return new ResponseEntity<>(new ApiResponse<>(
          false, "Error creating transaction", null, List.of(e.getMessage())),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
      return new ResponseEntity<>(new ApiResponse<>(
          true, "Transaction created successfully", transactionResponseDto, null),
          HttpStatus.CREATED);
  }


  @PostMapping(ApiStrings.LOAN_REVERSAL)
public ResponseEntity<ApiResponse<TransactionResponseDto>> reverse(
    @PathVariable UUID uuid) {
    try {
        TransactionResponseDto response = transactionService.reverseTransaction(uuid);
        return new ResponseEntity<>(
            new ApiResponse<>(true, "Transaction reversed successfully", response, null),
            HttpStatus.CREATED);
    } catch (EntityNotFoundException e) {
        return new ResponseEntity<>(
            new ApiResponse<>(false, "Transaction not found", null, List.of(e.getMessage())),
            HttpStatus.NOT_FOUND);
    } catch (TransactionSaveException e) {
        return new ResponseEntity<>(
            new ApiResponse<>(false, e.getMessage(), null, List.of(e.getMessage())),
            HttpStatus.BAD_REQUEST);
    }
}

  @PostMapping(ApiStrings.LOAN_REPAYMENT)
  public ResponseEntity<ApiResponse<TransactionResponseDto>> repayLoan(
      @PathVariable UUID uuid,
      @RequestBody @Valid TransactionRequestDto transactionDto) {
    TransactionResponseDto transactionResponseDto = null;
    try {
      log.debug("Creating loan repayment transaction with accounts and lines: {}", transactionDto.getTransactionLines().stream()
          .map(line -> line.getAccountId()).toList());
      transactionResponseDto = transactionService.createTransaction(transactionDto);
    } catch (EntityNotFoundException e) {
      log.error("Error creating loan repayment transaction", e);
      return new ResponseEntity<>(new ApiResponse<>(
          false, "Error creating loan repayment transaction", null, List.of(e.getMessage())),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(new ApiResponse<>(
        true, "Loan repayment transaction created successfully", transactionResponseDto, null),
        HttpStatus.CREATED);
  }

  @PostMapping(ApiStrings.LOAN_WRITEOFF)
  public ResponseEntity<ApiResponse<TransactionResponseDto>> writeOffLoan(
      @PathVariable UUID uuid,
      @RequestBody @Valid TransactionRequestDto transactionDto) {
    TransactionResponseDto transactionResponseDto = null;
    try {
      log.debug("Creating loan write-off transaction with accounts and lines: {}", transactionDto.getTransactionLines().stream()
          .map(line -> line.getAccountId()).toList());
      transactionResponseDto = transactionService.createTransaction(transactionDto);
    } catch (EntityNotFoundException e) {
      log.error("Error creating loan write-off transaction", e);
      return new ResponseEntity<>(new ApiResponse<>(
          false, "Error creating loan write-off transaction", null, List.of(e.getMessage())),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(new ApiResponse<>(
        true, "Loan write-off transaction created successfully", transactionResponseDto, null),
        HttpStatus.CREATED);
  }
}

  

