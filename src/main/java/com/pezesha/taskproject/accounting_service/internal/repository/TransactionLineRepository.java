package com.pezesha.taskproject.accounting_service.internal.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.pezesha.taskproject.accounting_service.internal.entity.TransactionLine;

@Repository
public interface TransactionLineRepository extends JpaRepository<TransactionLine, Long> {

  @Query("SELECT COALESCE(SUM(tl.debitAmount), 0) FROM TransactionLine tl " +
         "WHERE tl.account.id = :accountId " +
         "AND tl.transaction.reversedAt IS NULL " +
         "AND tl.createdAt <= :asOfDate")
  BigDecimal sumDebitAmountByAccountIdAndDate(@Param("accountId") Long accountId, 
                                               @Param("asOfDate") LocalDateTime asOfDate);

  @Query("SELECT COALESCE(SUM(tl.creditAmount), 0) FROM TransactionLine tl " +
         "WHERE tl.account.id = :accountId " +
         "AND tl.transaction.reversedAt IS NULL " +
         "AND tl.createdAt <= :asOfDate")
  BigDecimal sumCreditAmountByAccountIdAndDate(@Param("accountId") Long accountId, 
                                                @Param("asOfDate") LocalDateTime asOfDate);

  @Query("SELECT COALESCE(SUM(tl.debitAmount), 0) FROM TransactionLine tl " +
         "WHERE tl.account.id = :accountId " +
         "AND tl.transaction.reversedAt IS NULL")
  BigDecimal sumDebitAmountByAccountId(@Param("accountId") Long accountId);

  @Query("SELECT COALESCE(SUM(tl.creditAmount), 0) FROM TransactionLine tl " +
         "WHERE tl.account.id = :accountId " +
         "AND tl.transaction.reversedAt IS NULL")
  BigDecimal sumCreditAmountByAccountId(@Param("accountId") Long accountId);
}

