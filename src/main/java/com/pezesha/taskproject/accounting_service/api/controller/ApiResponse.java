package com.pezesha.taskproject.accounting_service.api.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
  private boolean success;
  private String message;
  private T data;
  private List<String> errors;
}
