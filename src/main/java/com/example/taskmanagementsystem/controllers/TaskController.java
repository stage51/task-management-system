package com.example.taskmanagementsystem.controllers;

import com.example.taskmanagementsystem.dtos.CommentDTO;
import com.example.taskmanagementsystem.dtos.TaskDTO;
import com.example.taskmanagementsystem.dtos.views.CommentViewDTO;
import com.example.taskmanagementsystem.dtos.views.TaskViewDTO;
import com.example.taskmanagementsystem.services.TaskService;
import com.example.taskmanagementsystem.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private TaskService taskService;
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public List<TaskViewDTO> getTasks(){
        logger.info("Handling GET request for /api/tasks");
        logger.info("Getting all tasks");
        logger.warn("Don't use this endpoint with big data. Use /api/tasks/page/{pageNumber}");
        return taskService.getAll();
    }
    @GetMapping("/page/{page}")
    public List<TaskViewDTO> getPageTasks(@PathVariable Integer page){
        logger.info("Handling GET request for /api/tasks/page/" + page);
        logger.info("Getting tasks in page " + page);
        return taskService.getPage(page).toList();
    }
    @GetMapping("/{id}")
    public TaskViewDTO getTaskById(@PathVariable Long id){
        logger.info("Handling GET request for /api/tasks/" + id);
        logger.info("Getting task with id " + id);
        return taskService.get(id);
    }
    @GetMapping("/{id}/comments")
    public List<CommentViewDTO> viewComments(@PathVariable Long id){
        logger.info("Handling GET request for /api/tasks/" + id + "/comments");
        logger.info("View all comments from task " + id);
        logger.warn("Don't use this endpoint with big data. Use /api/tasks/{id}/comments/page/{pageNumber}");
        return taskService.getComments(id);
    }
    @GetMapping("/{id}/comments/page/{pageNumber}")
    public List<CommentViewDTO> viewPageComments(@PathVariable Long id, @PathVariable Integer pageNumber){
        logger.info("Handling GET request for /api/tasks/" + id + "/comments");
        logger.info("View all comments from task " + id);
        return taskService.getPageComments(id, pageNumber).toList();
    }
    @PostMapping("/{id}/comments")
    public CommentViewDTO writeComments(@PathVariable Long id, @RequestBody CommentDTO dto, Principal principal){
        logger.info("Handling POST request for /api/tasks/" + id + "/comments");
        Long authorId = userService.getUserByPrincipal(principal).getId();
        logger.info("Write comment for task id " + id + ", author id " + authorId);
        return taskService.writeComment(id, authorId, dto);
    }
    @PutMapping("/{id}/change/status")
    public TaskViewDTO changeStatus(@PathVariable Long id, @RequestBody TaskDTO dto){
        logger.info("Handling PUT request for /api/tasks/" + id + "/change/status");
        logger.info("Updating task's status with id " + id);
        return taskService.changeStatus(id, dto);
    }
    // change
    @PostMapping("")
    public TaskViewDTO createTask(@RequestBody TaskDTO dto, Principal principal){
        logger.info("Handling POST request for /api/tasks");
        logger.info("Creating new task");
        dto.setAuthor(userService.getUserByPrincipal(principal).getId());
        return taskService.create(dto);
    }
    @PutMapping("/{id}")
    public TaskViewDTO updateTask(@PathVariable Long id, @RequestBody TaskDTO dto){
        logger.info("Handling PUT request for /api/tasks/" + id);
        logger.info("Updating task with id " + id);
        return taskService.update(id, dto);
    }
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){
        logger.info("Handling DELETE request for /api/tasks/" + id);
        logger.info("Deleting task with id " + id);
        taskService.delete(id);
    }
}
