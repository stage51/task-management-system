package com.example.taskmanagementsystem.controllers;

import com.example.taskmanagementsystem.dtos.CommentDTO;
import com.example.taskmanagementsystem.dtos.TaskDTO;
import com.example.taskmanagementsystem.dtos.views.CommentViewDTO;
import com.example.taskmanagementsystem.dtos.views.TaskViewDTO;
import com.example.taskmanagementsystem.exceptions.AppError;
import com.example.taskmanagementsystem.services.TaskService;
import com.example.taskmanagementsystem.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task", description = "Working with tasks")

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
    @Operation(summary = "Displaying all tasks. It is not recommended to use it. Instead, it is better to use a request via page.", tags = "Task")
    @GetMapping("")
    public List<TaskViewDTO> getTasks(){
        logger.info("Handling GET request for /api/tasks");
        logger.info("Getting all tasks");
        logger.warn("Don't use this endpoint with big data. Use /api/tasks/page/{pageNumber}");
        return taskService.getAll();
    }
    @Operation(summary = "Displaying all tasks in current page", tags = "Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TaskViewDTO.class)))),
            @ApiResponse(responseCode = "401", description = "BAD_REQUEST", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppError.class)))
    })
    @GetMapping("/page/{page}")
    public List<TaskViewDTO> getPageTasks(@PathVariable Integer page){
        logger.info("Handling GET request for /api/tasks/page/" + page);
        logger.info("Getting tasks in page " + page);
        return taskService.getPage(page).toList();
    }
    @Operation(summary = "Displaying task with current id", tags = "Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskViewDTO.class))),
            @ApiResponse(responseCode = "401", description = "BAD_REQUEST", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppError.class)))
    })
    @GetMapping("/{id}")
    public TaskViewDTO getTaskById(@PathVariable Long id){
        logger.info("Handling GET request for /api/tasks/" + id);
        logger.info("Getting task with id " + id);
        return taskService.get(id);
    }
    @Operation(summary = "Displaying task's comments. It is not recommended to use it. Instead, it is better to use a request via page.", tags = "Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentViewDTO.class)))),
            @ApiResponse(responseCode = "401", description = "BAD_REQUEST", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppError.class)))
    })
    @GetMapping("/{id}/comments")
    public List<CommentViewDTO> viewComments(@PathVariable Long id){
        logger.info("Handling GET request for /api/tasks/" + id + "/comments");
        logger.info("View all comments from task " + id);
        logger.warn("Don't use this endpoint with big data. Use /api/tasks/{id}/comments/page/{pageNumber}");
        return taskService.getComments(id);
    }
    @Operation(summary = "Displaying task's comments in current page", tags = "Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentViewDTO.class)))),
            @ApiResponse(responseCode = "401", description = "BAD_REQUEST", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppError.class)))
    })
    @GetMapping("/{id}/comments/page/{pageNumber}")
    public List<CommentViewDTO> viewPageComments(@PathVariable Long id, @PathVariable Integer pageNumber){
        logger.info("Handling GET request for /api/tasks/" + id + "/comments");
        logger.info("View all comments from task " + id);
        return taskService.getPageComments(id, pageNumber).toList();
    }
    @Operation(summary = "Creating task's comment", tags = "Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentViewDTO.class))),
            @ApiResponse(responseCode = "401", description = "BAD_REQUEST", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppError.class)))
    })
    @PostMapping("/{id}/comments")
    public CommentViewDTO writeComments(@PathVariable Long id, @RequestBody CommentDTO dto, Principal principal){
        logger.info("Handling POST request for /api/tasks/" + id + "/comments");
        Long authorId = userService.getUserByPrincipal(principal).getId();
        logger.info("Write comment for task id " + id + ", author id " + authorId);
        return taskService.writeComment(id, authorId, dto);
    }
    @Operation(summary = "Changing task's status", tags = "Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskViewDTO.class))),
            @ApiResponse(responseCode = "401", description = "BAD_REQUEST", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppError.class)))
    })
    @PutMapping("/{id}/change/status")
    public TaskViewDTO changeStatus(@PathVariable Long id, @RequestBody TaskDTO dto){
        logger.info("Handling PUT request for /api/tasks/" + id + "/change/status");
        logger.info("Updating task's status with id " + id);
        return taskService.changeStatus(id, dto);
    }
    @Operation(summary = "Creating new task", tags = "Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskViewDTO.class))),
            @ApiResponse(responseCode = "401", description = "BAD_REQUEST", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppError.class)))
    })
    @PostMapping("")
    public TaskViewDTO createTask(@RequestBody TaskDTO dto, Principal principal){
        logger.info("Handling POST request for /api/tasks");
        logger.info("Creating new task");
        dto.setAuthor(userService.getUserByPrincipal(principal).getId());
        return taskService.create(dto);
    }
    @Operation(summary = "Updating task with current id", tags = "Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskViewDTO.class))),
            @ApiResponse(responseCode = "401", description = "BAD_REQUEST", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppError.class)))
    })
    @PutMapping("/{id}")
    public TaskViewDTO updateTask(@PathVariable Long id, @RequestBody TaskDTO dto){
        logger.info("Handling PUT request for /api/tasks/" + id);
        logger.info("Updating task with id " + id);
        return taskService.update(id, dto);
    }
    @Operation(summary = "Deleting task with current id", tags = "Task")
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){
        logger.info("Handling DELETE request for /api/tasks/" + id);
        logger.info("Deleting task with id " + id);
        taskService.delete(id);
    }
}
