package com.example.taskmanagementsystem.dtos;

import com.example.taskmanagementsystem.constants.enums.Priority;
import com.example.taskmanagementsystem.constants.enums.Status;
import com.example.taskmanagementsystem.models.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class TaskDTO extends BaseDTO{
    @Length(min = 4, max = 30, message = "Title's size can't be less than 4 and more than 30")
    private String title;
    @Length(max = 200, message = "Title's size can't be more than 200")
    private String description;
    private Status status;
    private Priority priority;
    private Long author;
    private Long executor;
    private List<Long> comments;

    @Override
    public String toString() {
        return "Task {" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", author=" + author +
                ", executor=" + executor +
                ", comments=" + comments +
                ", id=" + id +
                '}';
    }
}
