package com.pezesha.taskproject.accounting_service.internal.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @Query("SELECT tl FROM TransactionLine tl " +
         "WHERE tl.account.id = :accountId " +
         "AND tl.transaction.reversedAt IS NULL " +
         "AND (:startDate IS NULL OR tl.createdAt >= :startDate) " +
         "AND (:endDate IS NULL OR tl.createdAt <= :endDate) " +
         "ORDER BY tl.createdAt DESC, tl.id DESC")
  Page<TransactionLine> findByAccountIdWithDateRange(
      @Param("accountId") Long accountId,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate,
      Pageable pageable);
}

