package com.example.taskmanagementsystem.repositories;

import com.example.taskmanagementsystem.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Page<User> findAll(Pageable pageable);
    Optional<User> findByEmail(String email);
}
