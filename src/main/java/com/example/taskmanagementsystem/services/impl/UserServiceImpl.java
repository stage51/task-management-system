package com.example.taskmanagementsystem.services.impl;

import com.example.taskmanagementsystem.dtos.RegistrationUserDTO;
import com.example.taskmanagementsystem.dtos.UserDTO;
import com.example.taskmanagementsystem.dtos.views.BaseViewDTO;
import com.example.taskmanagementsystem.dtos.views.TaskViewDTO;
import com.example.taskmanagementsystem.dtos.views.UserViewDTO;
import com.example.taskmanagementsystem.exceptions.EmptyPageException;
import com.example.taskmanagementsystem.exceptions.EntityInvalidException;
import com.example.taskmanagementsystem.exceptions.EntityNotFoundException;
import com.example.taskmanagementsystem.exceptions.PrincipalException;
import com.example.taskmanagementsystem.models.User;
import com.example.taskmanagementsystem.repositories.CommentRepository;
import com.example.taskmanagementsystem.repositories.TaskRepository;
import com.example.taskmanagementsystem.repositories.UserRepository;

import com.example.taskmanagementsystem.services.UserService;
import com.example.taskmanagementsystem.utils.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ValidationUtils validationUtils;
    private ModelMapper modelMapper;
    private TaskRepository taskRepository;
    private CommentRepository commentRepository;
    private PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserViewDTO create(RegistrationUserDTO dto) {
        if (!validationUtils.isValid(dto)){
            logger.error("User is invalid");
            validationUtils.violations(dto).stream().forEach(s -> logger.error(s.getMessage()));
            throw new EntityInvalidException("User isn't present");
        } else{
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
            logger.info("Created " + dto);
            return modelMapper.map(userRepository.saveAndFlush(modelMapper.map(dto, User.class)), UserViewDTO.class);
        }
    }
    @Deprecated
    @Override
    public UserViewDTO create(UserDTO dto) {
        if (!validationUtils.isValid(dto)){
            logger.error("User is invalid");
            validationUtils.violations(dto).stream().forEach(s -> logger.error(s.getMessage()));
            throw new EntityInvalidException("User is present");
        } else{
            logger.info("Created " + dto);
            return modelMapper.map(userRepository.saveAndFlush(modelMapper.map(dto, User.class)), UserViewDTO.class);
        }
    }

    @Override
    public UserViewDTO get(Long id) {
        return modelMapper.map(getUser(id), UserViewDTO.class);
    }
    private User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            logger.info("Received " + modelMapper.map(user.get(), UserViewDTO.class));
            return user.get();
        }
        else{
            logger.error("User with id " + id + " isn't found");
            throw new EntityNotFoundException("User", id);
        }
    }

    @Override
    public UserViewDTO update(Long id, UserDTO dto) {
        Optional<User> user = userRepository.findById(id);
        if (!validationUtils.isValid(dto)){
            logger.error("User is invalid");
            validationUtils.violations(dto).stream().forEach(s -> logger.error(s.getMessage()));
            throw new EntityInvalidException("User is invalid");
        } else if (!user.isPresent()){
            logger.error("User with id " + id + " isn't present");
            throw new EntityInvalidException("User isn't present");
        } else{
            User updatedUser = user.get();
            if (dto.getUsername() != null){
                updatedUser.setUsername(dto.getUsername());
            }
            if (dto.getEmail() != null){
                updatedUser.setEmail(dto.getEmail());
            }
            if (dto.getPassword() != null){
                updatedUser.setPassword(dto.getPassword());
            }
            if (dto.getComments() != null){
                updatedUser.setComments(commentRepository
                        .findAllById(dto.getComments()));
            }
            if (dto.getCreatedTasks() != null){
                updatedUser.setCreatedTasks(taskRepository
                        .findAllById(dto.getCreatedTasks()));
            }
            if (dto.getExecutableTasks() != null){
                updatedUser.setExecutableTasks(taskRepository
                        .findAllById(dto.getExecutableTasks()));
            }
            logger.info("Updated " + dto);
            return modelMapper.map(userRepository.saveAndFlush(updatedUser), UserViewDTO.class);
        }
    }

    @Override
    public void delete(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            logger.info("Deleted user with id" + id);
            userRepository.delete(user.get());
        }
        else{
            logger.error("User with id " + id + " isn't found");
            throw new EntityNotFoundException("User", id);
        }
    }

    @Override
    public List<UserViewDTO> getAll() {
        logger.info("Received all users");
        return userRepository.findAll().stream().map(s -> modelMapper.map(s, UserViewDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Page<UserViewDTO> getPage(int pageNumber) {
        Page<UserViewDTO> page = userRepository.findAll(PageRequest.of(pageNumber, PAGE_SIZE)).map(s -> modelMapper.map(s, UserViewDTO.class));
        if (isEmpty(page, pageNumber)){
            throw new EmptyPageException("User", pageNumber, page.getTotalPages());
        }
        logger.info("Received all users in page " + pageNumber);
        return page;
    }

    @Override
    public Page<TaskViewDTO> getAuthorTasks(Integer pageNumber, Long id) {
        Page<TaskViewDTO> page = taskRepository.findByAuthor(PageRequest.of(pageNumber, PAGE_SIZE), getUser(id))
                .map(s -> modelMapper.map(s, TaskViewDTO.class));
        if (isEmpty(page, pageNumber)){
            throw new EmptyPageException("User", pageNumber, page.getTotalPages());
        }
        logger.info("Received all author's tasks in page " + pageNumber);
        return page;
    }

    @Override
    public Page<TaskViewDTO> getExecutorTasks(Integer pageNumber, Long id) {
        Page<TaskViewDTO> page = taskRepository.findByExecutor(PageRequest.of(pageNumber, PAGE_SIZE), getUser(id))
                .map(s -> modelMapper.map(s, TaskViewDTO.class));
        if (isEmpty(page, pageNumber)){
            throw new EmptyPageException("User", pageNumber, page.getTotalPages());
        }
        logger.info("Received all executor's tasks in page " + pageNumber);
        return page;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserViewDTO getUserByPrincipal(Principal principal) {
        Optional<User> user = findByEmail(principal.getName());
        if(user.isPresent()){
            return modelMapper.map(user, UserViewDTO.class);
        } else{
            throw new PrincipalException("Principal isn't found");
        }

    }

    private boolean isEmpty(Page<? extends BaseViewDTO> page, int pageNumber){
        if ((page.getTotalPages() - 1) < pageNumber){
            logger.error("Page with users " + pageNumber + " isn't found");
            return true;
        }
        return false;
    }
}
