package com.example.taskmanagementsystem.controllers;

import com.example.taskmanagementsystem.dtos.CommentDTO;
import com.example.taskmanagementsystem.dtos.views.CommentViewDTO;
import com.example.taskmanagementsystem.services.CommentService;
import com.example.taskmanagementsystem.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comment", description = "Working with comments")
public class CommentController {
    private CommentService commentService;
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Operation(summary = "Displaying all comments. It is not recommended to use it. Instead, it is better to use a request via page.", tags = "Comment")
    @GetMapping("")
    public List<CommentViewDTO> getComments(){
        logger.info("Handling GET request for /api/comments");
        logger.info("Getting all comments");
        logger.warn("Don't use this endpoint with big data. Use /api/comments/page/{pageNumber}");
        return commentService.getAll();
    }
    @Operation(summary = "Displaying all comments in current page", tags = "Comment")
    @GetMapping("/page/{page}")
    public List<CommentViewDTO> getPageComments(@PathVariable Integer page){
        logger.info("Handling GET request for /api/comments/page/" + page);
        logger.info("Getting comments in page " + page);
        return commentService.getPage(page).toList();
    }
    @Operation(summary = "Displaying comment with current id", tags = "Comment")
    @GetMapping("/{id}")
    public CommentViewDTO getCommentById(@PathVariable Long id){
        logger.info("Handling GET request for /api/comments/" + id);
        logger.info("Getting comment with id " + id);
        return commentService.get(id);
    }
    @Operation(summary = "Creating new comment", tags = "Comment")
    @PostMapping("")
    public CommentViewDTO createComment(@RequestBody CommentDTO dto, Principal principal){
        logger.info("Handling POST request for /api/comments");
        logger.info("Creating new comment");
        dto.setAuthor(userService.getUserByPrincipal(principal).getId());
        return commentService.create(dto);
    }
    @Operation(summary = "Updating comment with current id", tags = "Comment")
    @PutMapping("/{id}")
    public CommentViewDTO updateComment(@PathVariable Long id, @RequestBody CommentDTO dto){
        logger.info("Handling PUT request for /api/comments/" + id);
        logger.info("Updating comment with id " + id);
        return commentService.update(id, dto);
    }
    @Operation(summary = "Deleting comment with current id", tags = "Comment")
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id){
        logger.info("Handling DELETE request for /api/comments/" + id);
        logger.info("Deleting comment with id " + id);
        commentService.delete(id);
    }
}
