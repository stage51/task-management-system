package com.example.taskmanagementsystem.dtos;

import com.example.taskmanagementsystem.models.User;
import com.example.taskmanagementsystem.utils.annotations.Unique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter

public class RegistrationUserDTO{
    @NotNull
    @Length(min = 4, max = 30, message = "Email's size can't be less than 4 and more than 30")
    @Email(message = "Wrong email format")
    @Unique(message = "User with this email is already exist", entity = User.class, field = "email")
    private String email;
    @NotNull
    @Length(min = 4, max = 30, message = "Username's size can't be less than 4 and more than 30")
    private String username;
    @NotNull
    @Length(min = 8, max = 30, message = "Password's size can't be less than 8 and more than 30")
    private String password;
    @NotNull
    private String confirmPassword;

    public RegistrationUserDTO(String email, String username, String password, String confirmPassword) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public RegistrationUserDTO() {

    }
}