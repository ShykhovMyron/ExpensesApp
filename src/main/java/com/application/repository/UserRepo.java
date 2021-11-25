package com.application.repository;

import com.application.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long > {
    boolean existsByUsername(String username);
    User findByUsername(String username);
}
