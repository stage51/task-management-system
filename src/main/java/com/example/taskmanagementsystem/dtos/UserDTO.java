package com.example.taskmanagementsystem.dtos;

import com.example.taskmanagementsystem.models.User;
import com.example.taskmanagementsystem.utils.annotations.Unique;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDTO extends BaseDTO{
    @Length(min = 4, max = 30, message = "Email's size can't be less than 4 and more than 30")
    @Email(message = "Wrong email format")
    @Unique(message = "User with this email is already exist", entity = User.class, field = "email")
    private String email;
    @Length(min = 4, max = 30, message = "Username's size can't be less than 4 and more than 30")
    private String username;

    @Length(min = 8, max = 30, message = "Password's size can't be less than 8 and more than 30")
    private String password;
    private List<Long> createdTasks;
    private List<Long> executableTasks;
    private List<Long> comments;

    @Override
    public String toString() {
        return "User {" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createdTasks=" + createdTasks +
                ", executableTasks=" + executableTasks +
                ", comments=" + comments +
                ", id=" + id +
                '}';
    }
}
