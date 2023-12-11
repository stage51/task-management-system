package com.example.taskmanagementsystem.dtos.views;

import com.example.taskmanagementsystem.constants.enums.Priority;
import com.example.taskmanagementsystem.constants.enums.Status;
import com.example.taskmanagementsystem.models.Comment;
import com.example.taskmanagementsystem.models.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class TaskViewDTO extends BaseViewDTO{
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private UserViewDTO author;
    private UserViewDTO executor;

    @Override
    public String toString() {
        return "TaskViewDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", author=" + author +
                ", executor=" + executor +
                ", id=" + id +
                '}';
    }
}
