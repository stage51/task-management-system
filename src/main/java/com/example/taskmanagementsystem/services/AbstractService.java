package com.example.taskmanagementsystem.services;

import org.springframework.data.domain.Page;

import java.util.List;

public interface AbstractService<T, K> {
    final int PAGE_SIZE = 20;
    K create(T dto);
    K get(Long id);
    K update(Long id, T dto);
    void delete(Long id);
    List<K> getAll();
    Page<K> getPage(int pageNumber);
}
