package com.application.repository;

import com.application.model.entity.ExpenseType;
import org.springframework.data.repository.CrudRepository;


public interface ExpenseTypeRepo extends CrudRepository<ExpenseType, Integer> {

    boolean existsByType(String type);

    ExpenseType findByType(String type);
}