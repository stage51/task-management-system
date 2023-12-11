package com.example.taskmanagementsystem.exceptions.handlers;

import com.example.taskmanagementsystem.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(new AppError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }
    @ExceptionHandler(EmptyPageException.class)
    public ResponseEntity<?> handleEmptyPageException(EmptyPageException ex) {
        return ResponseEntity.badRequest().body(new AppError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }
    @ExceptionHandler(PrincipalException.class)
    public ResponseEntity<?> handlePrincipalException(PrincipalException ex) {
        return ResponseEntity.badRequest().body(new AppError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }
    @ExceptionHandler(IncorrectLoginOrPasswordException.class)
    public ResponseEntity<?> handleIncorrectLoginOrPasswordException(IncorrectLoginOrPasswordException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AppError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
    }
    @ExceptionHandler(EntityInvalidException.class)
    public ResponseEntity<?> handleEntityIsInvalidException(EntityInvalidException ex){
        return ResponseEntity.badRequest().body(new AppError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }
}
