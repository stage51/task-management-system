package com.example.taskmanagementsystem.services.impl;

import com.example.taskmanagementsystem.dtos.CommentDTO;
import com.example.taskmanagementsystem.dtos.TaskDTO;
import com.example.taskmanagementsystem.dtos.views.CommentViewDTO;
import com.example.taskmanagementsystem.dtos.views.TaskViewDTO;
import com.example.taskmanagementsystem.exceptions.EmptyPageException;
import com.example.taskmanagementsystem.exceptions.EntityInvalidException;
import com.example.taskmanagementsystem.exceptions.EntityNotFoundException;
import com.example.taskmanagementsystem.models.Task;
import com.example.taskmanagementsystem.repositories.CommentRepository;
import com.example.taskmanagementsystem.repositories.TaskRepository;
import com.example.taskmanagementsystem.repositories.UserRepository;
import com.example.taskmanagementsystem.services.CommentService;
import com.example.taskmanagementsystem.services.TaskService;
import com.example.taskmanagementsystem.utils.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;
    private ValidationUtils validationUtils;
    private ModelMapper modelMapper;
    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private CommentService commentService;
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    @Autowired
    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    @Autowired
    public void setValidationUtils(ValidationUtils validationUtils) {
        this.validationUtils = validationUtils;
    }
    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {this.commentRepository = commentRepository;}
    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }
    @Override
    public TaskViewDTO create(TaskDTO dto) {
        if (!validationUtils.isValid(dto)){
            logger.error("Task is invalid");
            validationUtils.violations(dto).stream().forEach(s -> logger.error(s.getMessage()));
            throw new EntityInvalidException("Task is invalid");
        } else{
            logger.info("Created " + dto);
            Task task = modelMapper.map(dto, Task.class);
            if (dto.getAuthor() != null){
                task.setAuthor(userRepository.findById(dto.getAuthor()).get());
            }
            if (dto.getExecutor() != null){
                task.setExecutor(userRepository.findById(dto.getExecutor()).get());
            }
            return modelMapper.map(taskRepository.saveAndFlush(task), TaskViewDTO.class);
        }
    }

    @Override
    public TaskViewDTO get(Long id) {
        return modelMapper.map(this.isExist(id), TaskViewDTO.class);
    }

    @Override
    public TaskViewDTO update(Long id, TaskDTO dto) {
        Optional<Task> task = taskRepository.findById(id);
        if (!isValidOrPresent(id, task, dto)){
            throw new EntityInvalidException("Task is invalid");
        } else{
            Task updatedTask = task.get();
            if (dto.getTitle() != null){
                updatedTask.setTitle(dto.getTitle());
            }
            if (dto.getComments() != null){
                updatedTask.setComments(commentRepository
                        .findAllById(dto.getComments()));
            }
            if (dto.getAuthor() != null){
                updatedTask.setAuthor(userRepository.findById(dto.getAuthor()).get());
            }
            if (dto.getDescription() != null){
                updatedTask.setDescription(dto.getDescription());
            }
            if (dto.getExecutor() != null){
                updatedTask.setExecutor(userRepository.findById(dto.getExecutor()).get());
            }
            if (dto.getStatus() != null){
                updatedTask.setStatus(dto.getStatus());
            }
            if (dto.getPriority() != null){
                updatedTask.setPriority(dto.getPriority());
            }
            logger.info("Updated " + dto);
            return modelMapper.map(taskRepository.saveAndFlush(updatedTask), TaskViewDTO.class);
        }
    }

    @Override
    public void delete(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()){
            logger.info("Deleted task with id " + id);
            taskRepository.delete(task.get());
        }
        else{
            logger.error("Task with id " + id + " isn't found");
            throw new EntityNotFoundException("Task", id);
        }
    }

    @Override
    public List<TaskViewDTO> getAll() {
        logger.info("Received all tasks");
        return taskRepository.findAll().stream().map(s -> modelMapper.map(s, TaskViewDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Page<TaskViewDTO> getPage(int pageNumber) {
        Page<TaskViewDTO> page = taskRepository.findAll(PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("priority").ascending())).map(s -> modelMapper.map(s, TaskViewDTO.class));
        if ((page.getTotalPages() - 1) < pageNumber){
            logger.error("Page with tasks " + pageNumber + " isn't found");
            throw new EmptyPageException("Task", pageNumber, page.getTotalPages());
        }
        logger.info("Received all tasks in page " + pageNumber);
        return page;
    }

    @Override
    public List<CommentViewDTO> getComments(Long id) {
        Task task = this.isExist(id);
        logger.info("Received all comments in task with id " + id);
        return task.getComments().stream()
                .map(s -> modelMapper.map(s, CommentViewDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<CommentViewDTO> getPageComments(Long id, Integer pageNumber) {
        Task task = this.isExist(id);
        Page<CommentViewDTO> page = commentRepository.findByTask(PageRequest.of(pageNumber, PAGE_SIZE), task)
                .map(s -> modelMapper.map(s, CommentViewDTO.class));
        if ((page.getTotalPages() - 1) < pageNumber){
            logger.error("Page with tasks " + pageNumber + " isn't found");
            throw new EmptyPageException("Task", pageNumber, page.getTotalPages());
        }
        logger.info("Received all comments in task with id " + id + " in page " + pageNumber);
        return page;
    }

    @Override
    public CommentViewDTO writeComment(Long taskId, Long authorId, CommentDTO dto) {
        dto.setTask(taskId);
        dto.setAuthor(authorId);
        return commentService.create(dto);
    }

    @Override
    public TaskViewDTO changeStatus(Long id, TaskDTO dto) {
        Optional<Task> task = taskRepository.findById(id);
        if (!isValidOrPresent(id, task, dto)){
            throw new EntityInvalidException("Task is invalid");
        } else {
            Task updatedTask = task.get();
            if (dto.getStatus() != null){
                updatedTask.setStatus(dto.getStatus());
            }
            logger.info("Updated " + dto);
            return modelMapper.map(taskRepository.saveAndFlush(updatedTask), TaskViewDTO.class);
        }
    }

    private Task isExist(Long id){
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()){
            logger.info("Received " + modelMapper.map(task.get(), TaskViewDTO.class));
            return task.get();
        } else{
            logger.error("Task with id " + id + " isn't found");
            throw new EntityNotFoundException("Task", id);
        }
    }
    private boolean isValidOrPresent(Long id, Optional<Task> task, TaskDTO dto){
        if (!validationUtils.isValid(dto)){
            logger.error("Task is invalid");
            validationUtils.violations(dto).stream().forEach(s -> logger.error(s.getMessage()));
            return false;
        } else if (!task.isPresent()){
            logger.error("Task with id " + id + " isn't present");
            return false;
        } else{
            return true;
        }
    }
}
