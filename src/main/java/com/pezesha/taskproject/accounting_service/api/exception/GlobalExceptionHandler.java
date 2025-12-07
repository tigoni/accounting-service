package com.pezesha.taskproject.accounting_service.api.exception;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.pezesha.taskproject.accounting_service.api.controller.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler({ OptimisticLockingFailureException.class, ObjectOptimisticLockingFailureException.class })
  public ResponseEntity<ApiResponse<Object>> handleOptimisticLockingException(Exception ex) {
    return new ResponseEntity<>(
        new ApiResponse<>(false, "Concurrent modification detected. Please retry.", null,
            Arrays.asList("The resource was modified by another user. Please refresh and try again.")),
        HttpStatus.CONFLICT);
  }
}
