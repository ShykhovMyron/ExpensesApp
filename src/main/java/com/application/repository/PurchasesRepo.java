package com.application.repository;

import com.application.entity.Purchase;
import com.application.entity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;


public interface PurchasesRepo extends CrudRepository<Purchase, Integer> {

    // findAllByUserId и так все методы ниже
    Set<Purchase> findAllByUser_id(Integer tag);

    Page<Purchase> findAllByUser_id(Integer tag, Pageable pageable);


    Page<Purchase> findAllByUser_idAndType(Integer id, Type fitterByType, Pageable pageable);
}