package com.application.repository;

import com.application.model.entity.Expense;
import com.application.model.entity.ExpenseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;


public interface ExpenseRepo extends CrudRepository<Expense, Integer> {

    Optional<Expense> findById(Long id);

    // findAllByUserId и так все методы ниже
    Set<Expense> findAllByUserId(Long tag);

    Page<Expense> findAllByUserId(Long tag, Pageable pageable);


    Page<Expense> findAllByUserIdAndType(Long id, ExpenseType fitterByExpenseType, Pageable pageable);
}