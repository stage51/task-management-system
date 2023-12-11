package com.example.taskmanagementsystem.constants.enums;

public enum Status {
    WAITING(0), PROCESS(1), COMPLETED(2);

    private int status;
    private Status(int status){
        this.status = status;
    }
    @Override
    public String toString() {
        return String.valueOf(status);
    }
}
