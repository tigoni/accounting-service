package com.pezesha.taskproject.accounting_service.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pezesha.taskproject.accounting_service.internal.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
  Account findByAccountName(String accountName);
  Account findByAccountNameIgnoreCase(String accountName);
  Account findByNormalizedName(String normalizedName);
}
