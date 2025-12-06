package com.pezesha.taskproject.accounting_service.internal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

import org.hibernate.annotations.OnDelete;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "transaction_lines")
public class TransactionLine extends BaseEntityAudit {

  private String lineDescription;

  @ManyToOne
  @JoinColumn(name = "transaction_id", nullable = false)
  @OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
  private Transaction transaction;


  @Builder.Default
  @Column(precision = 18, scale = 2)
  private BigDecimal debitAmount = BigDecimal.ZERO;

  @Builder.Default
  @Column(precision = 18, scale = 2)
  private BigDecimal creditAmount = BigDecimal.ZERO;
}
