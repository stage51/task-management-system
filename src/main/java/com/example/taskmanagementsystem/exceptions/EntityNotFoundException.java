package com.example.taskmanagementsystem.exceptions;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String entity, Long id){
        super(entity + " with id " + id + " is not found");
    }
}
