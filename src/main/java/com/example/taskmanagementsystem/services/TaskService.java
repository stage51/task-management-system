package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.dtos.CommentDTO;
import com.example.taskmanagementsystem.dtos.TaskDTO;
import com.example.taskmanagementsystem.dtos.views.CommentViewDTO;
import com.example.taskmanagementsystem.dtos.views.TaskViewDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService extends AbstractService<TaskDTO, TaskViewDTO>{
    List<CommentViewDTO> getComments(Long id);
    Page<CommentViewDTO> getPageComments(Long id, Integer pageNumber);
    CommentViewDTO writeComment(Long taskId, Long authorId, CommentDTO dto);
    TaskViewDTO changeStatus(Long id, TaskDTO dto);
}
