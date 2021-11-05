package com.application.repository;

import com.application.entity.Purchases;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


public interface PurchasesRepo extends CrudRepository<Purchases, Integer> {

    Page<Purchases> findByType(String tag,Pageable pageable);

    Page<Purchases> findAllByUser_id(Integer tag, Pageable pageable);

}
