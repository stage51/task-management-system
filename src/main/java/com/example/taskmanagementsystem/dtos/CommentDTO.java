package com.example.taskmanagementsystem.dtos;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@RequiredArgsConstructor
public class CommentDTO extends BaseDTO{
    @Length(min = 4, max = 200, message = "Comment's size can't be less than 4 and more than 200")
    private String content;
    private Long author;
    private Long task;

    @Override
    public String toString() {
        return "Comment {" +
                "content='" + content + '\'' +
                ", author=" + author +
                ", task=" + task +
                ", id=" + id +
                '}';
    }
}
