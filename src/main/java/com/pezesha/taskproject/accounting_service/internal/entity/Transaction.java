package com.pezesha.taskproject.accounting_service.internal.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class Transaction extends BaseEntityAudit {

  @Version
  @Column(name = "version", nullable = false)
  private Long version;

  private String description;
  @Column(unique = true)
  private String idempotencyKey;

  @Column(length = 3)
  private String currency;

  @JsonManagedReference
  @OneToMany(
      mappedBy = "transaction",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private List<TransactionLine> lines;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "reversed_transaction_id")
private Transaction reversedTransaction;

@Column(name = "reversed_at")
private LocalDateTime reversedAt;


}
