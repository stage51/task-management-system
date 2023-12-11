package com.example.taskmanagementsystem.exceptions;

public class IncorrectLoginOrPasswordException extends RuntimeException{
    public IncorrectLoginOrPasswordException(String message) {
        super(message);
    }
}
