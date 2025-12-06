package com.pezesha.taskproject.accounting_service.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.OnDelete;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class Transaction extends BaseEntityAudit {

  @Column(length = 3)
  private String currency;

  @JsonManagedReference
  @OneToMany(
      mappedBy = "transaction",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private List<TransactionLine> lines;

  @ManyToOne
  @JoinColumn(name = "account_id")
  @OnDelete(action = org.hibernate.annotations.OnDeleteAction.NO_ACTION)
  private Account account;

  private String description;
}
