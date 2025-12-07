package com.pezesha.taskproject.accounting_service.api.controller;

import com.pezesha.taskproject.accounting_service.internal.utils.ApiStrings;
import com.pezesha.taskproject.accounting_service.api.dto.TransactionRequestDto;
import com.pezesha.taskproject.accounting_service.api.dto.TransactionResponseDto;
import com.pezesha.taskproject.accounting_service.api.exception.TransactionSaveException;
import com.pezesha.taskproject.accounting_service.internal.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;
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


  @PostMapping(ApiStrings.CREATE_TRANSACTION)
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


  @PostMapping(ApiStrings.REVERSE_TRANSACTION)
public ResponseEntity<ApiResponse<TransactionResponseDto>> reverse(
    @PathVariable String idempotencyKey) {
    try {
        TransactionResponseDto response = transactionService.reverseTransaction(idempotencyKey);
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
}

  

