package com.example.taskmanagementsystem.dtos.views;

import com.example.taskmanagementsystem.dtos.BaseDTO;
import com.example.taskmanagementsystem.models.Comment;
import com.example.taskmanagementsystem.models.Task;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class UserViewDTO extends BaseViewDTO{
    private String email;
    private String username;

    @Override
    public String toString() {
        return "UserViewDTO{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", id=" + id +
                '}';
    }
}
