package com.example.taskmanagementsystem.exceptions;

public class EmptyPageException extends RuntimeException{
    public EmptyPageException(String entity, Integer pageNumber, Integer totalPages) {
        super(entity + " page with number " + pageNumber +
                " is empty, because page number less than 0 or there are only " + totalPages + " pages");
    }
}
