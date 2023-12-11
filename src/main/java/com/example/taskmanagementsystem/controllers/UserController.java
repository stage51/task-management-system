package com.example.taskmanagementsystem.controllers;

import com.example.taskmanagementsystem.dtos.UserDTO;
import com.example.taskmanagementsystem.dtos.views.TaskViewDTO;
import com.example.taskmanagementsystem.dtos.views.UserViewDTO;
import com.example.taskmanagementsystem.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Working with users")
public class UserController {
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Operation(summary = "Displaying all users. It is not recommended to use it. Instead, it is better to use a request via page.", tags = "User")
    @GetMapping("")
    public List<UserViewDTO> getUsers(){
        logger.info("Handling GET request for /api/users");
        logger.info("Getting all users");
        logger.warn("Don't use this endpoint with big data. Use /api/users/page/{pageNumber}");
        return userService.getAll();
    }
    @Operation(summary = "Displaying all users in current page", tags = "User")
    @GetMapping("/page/{page}")
    public List<UserViewDTO> getPageUsers(@PathVariable Integer page){
        logger.info("Handling GET request for /api/users/page/" + page);
        logger.info("Getting users in page " + page);
        return userService.getPage(page).toList();
    }
    @Operation(summary = "Displaying user with current id", tags = "User")
    @GetMapping("/{id}")
    public UserViewDTO getUserById(@PathVariable Long id){
        logger.info("Handling GET request for /api/users/" + id);
        logger.info("Getting user with id " + id);
        return userService.get(id);
    }
    @Operation(summary = "Displaying user's created tasks in current page", tags = "User")
    @GetMapping("/{id}/tasks/created/page/{page}")
    public List<TaskViewDTO> getAuthorTasks(@PathVariable Long id, @PathVariable Integer page){
        logger.info("Handling GET request for /api/users/" + id + "/tasks/created/page/" + page);
        logger.info("Getting author's tasks with id " + id);
        return userService.getAuthorTasks(page, id).toList();
    }
    @Operation(summary = "Displaying tasks performed by the user in current page", tags = "User")
    @GetMapping("/{id}/tasks/executable/page/{page}")
    public List<TaskViewDTO> getExecutorTasks(@PathVariable Long id, @PathVariable Integer page){
        logger.info("Handling GET request for /api/users/" + id + "/tasks/executable/page/" + page);
        logger.info("Getting executor's tasks with id " + id);
        return userService.getExecutorTasks(page, id).toList();
    }
    @Operation(summary = "Creating new user", tags = "User")
    @PostMapping("")
    public UserViewDTO createUser(@RequestBody UserDTO dto){
        logger.info("Handling POST request for /api/users");
        logger.info("Creating new user");
        return userService.create(dto);
    }
    @Operation(summary = "Updating user with current id", tags = "User")
    @PutMapping("/{id}")
    public UserViewDTO updateUser(@PathVariable Long id, @RequestBody UserDTO dto){
        logger.info("Handling PUT request for /api/users/" + id);
        logger.info("Updating user with id " + id);
        return userService.update(id, dto);
    }
    @Operation(summary = "Deleting user with current id", tags = "User")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        logger.info("Handling DELETE request for /api/users/" + id);
        logger.info("Deleting user with id " + id);
        userService.delete(id);
    }
}
