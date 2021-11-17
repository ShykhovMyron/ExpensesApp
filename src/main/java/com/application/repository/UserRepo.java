package com.application.repository;

import com.application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long > {
    User findByUsername(String username);

    User getById(Integer userId);
}
