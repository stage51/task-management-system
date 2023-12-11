package com.example.taskmanagementsystem.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ValidationUtilsImpl implements ValidationUtils {

    private final Validator validator;

    @Autowired
    public ValidationUtilsImpl(Validator validator) {
        this.validator = validator;
    }


    @Override
    public <E> boolean isValid(E object) {
        return this.validator.validate(object).size() == 0;
    }

    @Override
    public <E> Set<ConstraintViolation<E>> violations(E object) {
        return this.validator.validate(object);
    }
}
