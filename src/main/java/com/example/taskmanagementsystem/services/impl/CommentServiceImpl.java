package com.example.taskmanagementsystem.services.impl;

import com.example.taskmanagementsystem.dtos.CommentDTO;
import com.example.taskmanagementsystem.dtos.views.CommentViewDTO;
import com.example.taskmanagementsystem.exceptions.EmptyPageException;
import com.example.taskmanagementsystem.exceptions.EntityInvalidException;
import com.example.taskmanagementsystem.exceptions.EntityNotFoundException;
import com.example.taskmanagementsystem.models.Comment;
import com.example.taskmanagementsystem.repositories.CommentRepository;
import com.example.taskmanagementsystem.repositories.TaskRepository;
import com.example.taskmanagementsystem.repositories.UserRepository;
import com.example.taskmanagementsystem.services.CommentService;
import com.example.taskmanagementsystem.utils.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private ValidationUtils validationUtils;
    private ModelMapper modelMapper;
    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
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
    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public CommentViewDTO create(CommentDTO dto) {
        if (!validationUtils.isValid(dto)){
            logger.error("Comment is invalid");
            validationUtils.violations(dto).stream().forEach(s -> logger.error(s.getMessage()));
            throw new EntityInvalidException("Comment is invalid");
        } else{
            logger.info("Created " + dto);
            Comment comment = modelMapper.map(dto, Comment.class);
            comment.setTask(taskRepository.findById(dto.getTask()).get());
            comment.setAuthor(userRepository.findById(dto.getAuthor()).get());
            return modelMapper.map(commentRepository.saveAndFlush(comment), CommentViewDTO.class);
        }
    }

    @Override
    public CommentViewDTO get(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isPresent()){
            logger.info("Received " + modelMapper.map(comment.get(), CommentViewDTO.class));
            return modelMapper.map(comment.get(), CommentViewDTO.class);
        }
        else{
            logger.error("Comment with id " + id + " isn't found");
            throw new EntityNotFoundException("Comment", id);
        }
    }

    @Override
    public CommentViewDTO update(Long id, CommentDTO dto) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (!validationUtils.isValid(dto)){
            logger.error("Comment is invalid");
            validationUtils.violations(dto).stream().forEach(s -> logger.error(s.getMessage()));
            throw new EntityInvalidException("Comment is invalid");
        } else if (!comment.isPresent()){
            logger.error("Comment with id " + id + " isn't present");
            throw new EntityInvalidException("Comment isn't present");
        } else{
            Comment updatedComment = comment.get();
            if(dto.getAuthor() != null){
                updatedComment.setAuthor(userRepository.findById(dto.getAuthor()).get());
            }
            if(dto.getContent() != null){
                updatedComment.setContent(dto.getContent());
            }
            if(dto.getTask() != null){
                updatedComment.setTask(taskRepository.findById(dto.getTask()).get());
            }
            logger.info("Updated " + dto);
            return modelMapper.map(commentRepository.saveAndFlush(updatedComment), CommentViewDTO.class);
        }
    }

    @Override
    public void delete(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isPresent()){
            logger.info("Deleted comment with id " + id);
            commentRepository.delete(comment.get());
        }
        else{
            logger.error("Comment with id " + id + " isn't found");
            throw new EntityNotFoundException("Comment", id);
        }
    }

    @Override
    public List<CommentViewDTO> getAll() {
        logger.info("Received all comments");
        return commentRepository.findAll().stream().map(s -> modelMapper.map(s, CommentViewDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Page<CommentViewDTO> getPage(int pageNumber) {
        Page<CommentViewDTO> page = commentRepository.findAll(PageRequest.of(pageNumber, PAGE_SIZE)).map(s -> modelMapper.map(s, CommentViewDTO.class));
        if ((page.getTotalPages() - 1) < pageNumber){
            logger.error("Page with comments " + pageNumber + " isn't found");
            throw new EmptyPageException("Comment", pageNumber, page.getTotalPages());
        }
        logger.info("Received all comments in page " + pageNumber);
        return page;
    }
}
