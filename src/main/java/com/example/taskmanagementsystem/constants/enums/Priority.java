package com.example.taskmanagementsystem.constants.enums;

public enum Priority {
    HIGH(0), MIDDLE(1), LOW(2);

    private int priority;
    private Priority(int priority){
        this.priority = priority;
    }
    @Override
    public String toString() {
        return String.valueOf(priority);
    }
}
