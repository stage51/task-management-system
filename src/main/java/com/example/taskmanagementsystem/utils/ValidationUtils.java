package com.example.taskmanagementsystem.utils;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

public interface ValidationUtils {
    <E> boolean isValid(E object);

    <E> Set<ConstraintViolation<E>> violations(E object);
}