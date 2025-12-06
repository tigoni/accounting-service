package com.pezesha.taskproject.accounting_service.internal.service;

import com.pezesha.taskproject.accounting_service.api.exception.TransactionSaveException;
import com.pezesha.taskproject.accounting_service.internal.entity.BaseEntityAudit; 
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public class BasicService<T extends BaseEntityAudit, U extends JpaRepository<T, Long>> {

  protected U repository;

  public BasicService(@Autowired U repository) {
    this.repository = repository;
  }

  public T persist(T model) throws IllegalArgumentException, TransactionSaveException {
    return repository.save(model);
  }

  public Optional<T> findEntity(Long id) {
    return repository.findById(id);
  }


  public List<T> findAll() {
    return repository.findAll();
  }
}
