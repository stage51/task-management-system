package com.example.taskmanagementsystem.repositories;

import com.example.taskmanagementsystem.models.Task;
import com.example.taskmanagementsystem.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findById(Long id);

    Page<Task> findAll(Pageable pageable);
    Page<Task> findByAuthor(Pageable pageable, User author);
    Page<Task> findByExecutor(Pageable pageable, User executor);
}
