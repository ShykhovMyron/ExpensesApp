package com.application.repository;

import com.application.entity.Purchase;
import com.application.entity.PurchaseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;


public interface PurchasesRepo extends CrudRepository<Purchase, Integer> {

    Optional<Purchase> findById(Long id);
    // findAllByUserId и так все методы ниже
    Set<Purchase> findAllByUser_id(Long tag);

    Page<Purchase> findAllByUser_id(Long tag, Pageable pageable);


    Page<Purchase> findAllByUser_idAndType(Long  id, PurchaseType fitterByPurchaseType, Pageable pageable);
}