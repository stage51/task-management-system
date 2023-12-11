package com.example.taskmanagementsystem.controllers;

import com.example.taskmanagementsystem.dtos.CommentDTO;
import com.example.taskmanagementsystem.dtos.views.CommentViewDTO;
import com.example.taskmanagementsystem.services.CommentService;
import com.example.taskmanagementsystem.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
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

    @GetMapping("")
    public List<CommentViewDTO> getComments(){
        logger.info("Handling GET request for /api/comments");
        logger.info("Getting all comments");
        logger.warn("Don't use this endpoint with big data. Use /api/comments/page/{pageNumber}");
        return commentService.getAll();
    }
    @GetMapping("/page/{page}")
    public List<CommentViewDTO> getPageComments(@PathVariable Integer page){
        logger.info("Handling GET request for /api/comments/page/" + page);
        logger.info("Getting comments in page " + page);
        return commentService.getPage(page).toList();
    }
    @GetMapping("/{id}")
    public CommentViewDTO getCommentById(@PathVariable Long id){
        logger.info("Handling GET request for /api/comments/" + id);
        logger.info("Getting comment with id " + id);
        return commentService.get(id);
    }
    // change
    @PostMapping("")
    public CommentViewDTO createComment(@RequestBody CommentDTO dto, Principal principal){
        logger.info("Handling POST request for /api/comments");
        logger.info("Creating new comment");
        dto.setAuthor(userService.getUserByPrincipal(principal).getId());
        return commentService.create(dto);
    }
    @PutMapping("/{id}")
    public CommentViewDTO updateComment(@PathVariable Long id, @RequestBody CommentDTO dto){
        logger.info("Handling PUT request for /api/comments/" + id);
        logger.info("Updating comment with id " + id);
        return commentService.update(id, dto);
    }
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id){
        logger.info("Handling DELETE request for /api/comments/" + id);
        logger.info("Deleting comment with id " + id);
        commentService.delete(id);
    }
}
