package com.pezesha.taskproject.accounting_service.internal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.math.BigDecimal;

import org.hibernate.annotations.OnDelete;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "transaction_lines")
public class TransactionLine extends BaseEntityAudit {

  @Version
  @Column(name = "version", nullable = false)
  private Long version;

  @ManyToOne
  @JoinColumn(name = "transaction_id", nullable = false)
  @OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
  private Transaction transaction;


  @ManyToOne
  @JoinColumn(name = "account_id")
  @OnDelete(action = org.hibernate.annotations.OnDeleteAction.NO_ACTION)
  private Account account;

  @Builder.Default
  @Column(precision = 18, scale = 2)
  private BigDecimal debitAmount = BigDecimal.ZERO;

  @Builder.Default
  @Column(precision = 18, scale = 2)
  private BigDecimal creditAmount = BigDecimal.ZERO;
}
