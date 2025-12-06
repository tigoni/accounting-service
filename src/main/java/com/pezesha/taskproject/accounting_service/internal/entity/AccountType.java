package com.pezesha.taskproject.accounting_service.internal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "account_types")
@AttributeOverride(name = "id", column = @Column(name = "account_id"))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AccountType extends BaseEntity {

  private Integer categoryId;
  private String accountNumber;
  private String accountName;
  private String accountDescription;

  public boolean isAsset() {
    return this.categoryId == 1;
  }

  public boolean isLiability() {
    return this.categoryId == 2;
  }

  public boolean isEquity() {
    return this.categoryId == 3;
  }

  public boolean isRevenue() {
    return this.categoryId == 4;
  }

  public boolean isExpense() {
    return this.categoryId == 5;
  }
}
