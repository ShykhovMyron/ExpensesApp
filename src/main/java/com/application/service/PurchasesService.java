package com.application.service;

import com.application.entity.Purchases;
import com.application.repository.PurchasesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PurchasesService {
    @Autowired
    private PurchasesRepo purchasesRepo;
}
