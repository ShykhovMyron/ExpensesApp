package com.application.repository;

import com.application.model.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;


public interface ExpenseRepo extends CrudRepository<Expense, Long> {

    Set<Expense> findAllByUserId(Long tag);

    Page<Expense> findAllByUserId(Long tag, Pageable pageable);
}