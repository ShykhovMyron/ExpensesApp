package com.application.repository;

import com.application.model.entity.ExpenseType;
import com.application.model.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepo extends JpaRepository<Wallet, Long> {
    Wallet getWalletByUserId(Long userId);
}
