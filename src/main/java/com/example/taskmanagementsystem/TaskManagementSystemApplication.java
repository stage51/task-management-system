package com.example.taskmanagementsystem;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Task Management System",
                version = "0.0.1-SNAPSHOT",
                description = "Простое REST приложение для работы с задачами, с регистрацией, авторизацией и возможностью комментирования"
        )
)
@SpringBootApplication
public class TaskManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskManagementSystemApplication.class, args);
    }

}
