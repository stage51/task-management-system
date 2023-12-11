package com.example.taskmanagementsystem.dtos.views;

import com.example.taskmanagementsystem.dtos.BaseDTO;
import com.example.taskmanagementsystem.models.Task;
import com.example.taskmanagementsystem.models.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@RequiredArgsConstructor
public class CommentViewDTO extends BaseViewDTO{
    private String content;
    private UserViewDTO author;
    private TaskViewDTO task;

    @Override
    public String toString() {
        return "CommentViewDTO{" +
                "content='" + content + '\'' +
                ", author=" + author +
                ", task=" + task +
                ", id=" + id +
                '}';
    }
}
