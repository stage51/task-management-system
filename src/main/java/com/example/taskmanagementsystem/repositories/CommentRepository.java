package com.example.taskmanagementsystem.repositories;

import com.example.taskmanagementsystem.models.Comment;
import com.example.taskmanagementsystem.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByTask(Pageable pageable, Task task);
}
