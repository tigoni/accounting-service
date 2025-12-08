package com.pezesha.taskproject.accounting_service.internal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "accounts")
public class Account extends BaseEntityAudit {

  protected Long id;
  protected String accountNumber;
  protected String accountName;
  protected String normalizedName;
  protected String accountDescription;

  @ManyToOne
  @JoinColumn(name = "account_type_id", nullable = false)
  protected AccountType accountType;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_account_id")
  private Account parentAccount;
}
