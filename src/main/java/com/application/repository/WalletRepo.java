package com.application.repository;

import com.application.entity.User;
import com.application.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepo extends JpaRepository<Wallet,Long > {
    Wallet findByUserId(Integer userId);

    Wallet getById(Integer userId);
}
