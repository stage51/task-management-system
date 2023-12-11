package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.dtos.RegistrationUserDTO;
import com.example.taskmanagementsystem.dtos.UserDTO;
import com.example.taskmanagementsystem.dtos.views.TaskViewDTO;
import com.example.taskmanagementsystem.dtos.views.UserViewDTO;
import com.example.taskmanagementsystem.models.User;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.Optional;

public interface UserService extends AbstractService<UserDTO, UserViewDTO>{
    UserViewDTO create(RegistrationUserDTO dto);
    Page<TaskViewDTO> getAuthorTasks(Integer pageNumber, Long id);
    Page<TaskViewDTO> getExecutorTasks(Integer pageNumber, Long id);
    Optional<User> findByEmail(String email);
    UserViewDTO getUserByPrincipal(Principal principal);
}
