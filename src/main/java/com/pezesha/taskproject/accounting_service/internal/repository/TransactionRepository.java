package com.pezesha.taskproject.accounting_service.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pezesha.taskproject.accounting_service.internal.entity.Account;
import com.pezesha.taskproject.accounting_service.internal.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  Transaction findByAccount(Account account);
}
