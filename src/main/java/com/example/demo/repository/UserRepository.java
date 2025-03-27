package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {  // <- Добавлено!

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}