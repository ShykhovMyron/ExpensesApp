package com.application.repository;

import com.application.entity.Purchase;
import com.application.entity.PurchaseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;


public interface PurchaseTypeRepo extends CrudRepository<PurchaseType, Integer> {
    PurchaseType findByType(String type);
}