package com.application.repository;

import com.application.entity.Purchase;
import com.application.entity.PurchaseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;


public interface PurchaseRepo extends CrudRepository<Purchase, Integer> {

    Optional<Purchase> findById(Long id);
    // findAllByUserId и так все методы ниже
    Set<Purchase> findAllByUserId(Long tag);

    Page<Purchase> findAllByUserId(Long tag, Pageable pageable);


    Page<Purchase> findAllByUserIdAndType(Long  id, PurchaseType fitterByPurchaseType, Pageable pageable);
}