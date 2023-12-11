package com.example.taskmanagementsystem.exceptions;

public class EntityInvalidException extends RuntimeException{
    public EntityInvalidException(String message) {
        super(message);
    }
}
