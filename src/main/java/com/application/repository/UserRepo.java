package com.application.repository;

import com.application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,String > {
    User findByUsername(String username);
}
