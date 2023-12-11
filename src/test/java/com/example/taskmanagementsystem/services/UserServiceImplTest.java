package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.dtos.RegistrationUserDTO;
import com.example.taskmanagementsystem.dtos.UserDTO;
import com.example.taskmanagementsystem.dtos.views.TaskViewDTO;
import com.example.taskmanagementsystem.dtos.views.UserViewDTO;
import com.example.taskmanagementsystem.exceptions.EntityInvalidException;
import com.example.taskmanagementsystem.models.Task;
import com.example.taskmanagementsystem.models.User;
import com.example.taskmanagementsystem.repositories.CommentRepository;
import com.example.taskmanagementsystem.repositories.TaskRepository;
import com.example.taskmanagementsystem.repositories.UserRepository;
import com.example.taskmanagementsystem.services.impl.TaskServiceImpl;
import com.example.taskmanagementsystem.services.impl.UserServiceImpl;
import com.example.taskmanagementsystem.utils.ValidationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;


import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TaskService taskService;
    @Captor
    private ArgumentCaptor<User> userCaptor;

    @BeforeEach
    void setUp() {
        // Инициализация Mockito
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        // Создание мок-объектов
        RegistrationUserDTO registrationUserDTO = new RegistrationUserDTO();
        User userEntity = new User();
        UserViewDTO userViewDTO = new UserViewDTO();

        // Настройка поведения моков
        when(validationUtils.isValid(registrationUserDTO)).thenReturn(true);
        when(modelMapper.map(registrationUserDTO, User.class)).thenReturn(userEntity);
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        when(userRepository.saveAndFlush(any())).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserViewDTO.class)).thenReturn(userViewDTO);

        // Вызов тестируемого метода
        UserViewDTO result = userService.create(registrationUserDTO);

        // Проверки
        assertNotNull(result);
        assertEquals(userViewDTO, result);

        // Проверка вызовов методов
        verify(validationUtils, times(1)).isValid(registrationUserDTO);
        verify(modelMapper, times(1)).map(registrationUserDTO, User.class);
        verify(passwordEncoder, times(1)).encode(any());
        verify(userRepository, times(1)).saveAndFlush(userCaptor.capture());
        verify(modelMapper, times(1)).map(userCaptor.getValue(), UserViewDTO.class);
    }

    @Test
    void testGetUser() {
        // Создание мок-объектов
        Long userId = 1L;
        User userEntity = new User();
        UserViewDTO userViewDTO = new UserViewDTO();

        // Настройка поведения моков
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(modelMapper.map(userEntity, UserViewDTO.class)).thenReturn(userViewDTO);

        // Вызов тестируемого метода
        UserViewDTO result = userService.get(userId);

        // Проверки
        assertNotNull(result);
        assertEquals(userViewDTO, result);
    }
    @Test
    void testUpdateUser () {
        // Создание мок-объектов
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        User userEntity = new User();
        UserViewDTO userViewDTO = new UserViewDTO();

        // Настройка поведения моков
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(validationUtils.isValid(userDTO)).thenReturn(true);
        when(userRepository.saveAndFlush(any())).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserViewDTO.class)).thenReturn(userViewDTO);

        // Вызов тестируемого метода
        UserViewDTO result = userService.update(userId, userDTO);

        // Проверки
        assertNotNull(result);
        assertEquals(userViewDTO, result);

        // Проверка вызовов методов
        verify(userRepository, times(1)).findById(userId);
        verify(validationUtils, times(1)).isValid(userDTO);
        verify(userRepository, times(1)).saveAndFlush(userCaptor.capture());
        verify(modelMapper, times(1)).map(userCaptor.getValue(), UserViewDTO.class);
    }

    @Test
    void testDeleteUser () {
        // Создание мок-объектов
        Long userId = 1L;
        User userEntity = new User();

        // Настройка поведения моков
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // Вызов тестируемого метода
        userService.delete(userId);

        // Проверка вызовов методов
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(userEntity);
    }

    @Test
    void testGetAllUsers() {
        // Создание мок-объектов
        List<User> userList = Arrays.asList(new User(), new User());
        List<UserViewDTO> userViewDTOList = Arrays.asList(new UserViewDTO(), new UserViewDTO());

        // Настройка поведения моков
        when(userRepository.findAll()).thenReturn(userList);
        when(modelMapper.map(any(), eq(UserViewDTO.class))).thenReturn(userViewDTOList.get(0), userViewDTOList.get(1));

        // Вызов тестируемого метода
        List<UserViewDTO> result = userService.getAll();

        // Проверки
        assertNotNull(result);
        assertEquals(userViewDTOList.size(), result.size());

        // Проверка вызовов методов
        verify(userRepository, times(1)).findAll();
        verify(modelMapper, times(userList.size())).map(any(), eq(UserViewDTO.class));
    }

    @Test
    void testGetPageOfUsers () {
        // Создание мок-объектов
        int pageNumber = 0;
        Page<User> userPage = new PageImpl<>(Arrays.asList(new User(), new User()));
        Page<UserViewDTO> userViewDTOPage = new PageImpl<>(Arrays.asList(new UserViewDTO(), new UserViewDTO()));

        // Настройка поведения моков
        when(userRepository.findAll(PageRequest.of(pageNumber, UserService.PAGE_SIZE))).thenReturn(userPage);
        when(modelMapper.map(any(), eq(UserViewDTO.class))).thenReturn(userViewDTOPage.getContent().get(0), userViewDTOPage.getContent().get(1));

        // Вызов тестируемого метода
        Page<UserViewDTO> result = userService.getPage(pageNumber);

        // Проверки
        assertNotNull(result);
        assertEquals(userViewDTOPage.getTotalElements(), result.getTotalElements());

        // Проверка вызовов методов
        verify(userRepository, times(1)).findAll(PageRequest.of(pageNumber, UserService.PAGE_SIZE));
        verify(modelMapper, times(userViewDTOPage.getContent().size())).map(any(), eq(UserViewDTO.class));
    }
    @Test
    void testGetUserByPrincipal() {
        // Создание мок-объектов
        Principal principal = Mockito.mock(Principal.class);
        Optional<User> optionalUser = Optional.of(new User());
        UserViewDTO userViewDTO = new UserViewDTO();

        // Настройка поведения моков
        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(optionalUser);
        when(modelMapper.map(optionalUser, UserViewDTO.class)).thenReturn(userViewDTO);

        // Вызов тестируемого метода
        UserViewDTO result = userService.getUserByPrincipal(principal);

        // Проверки
        assertNotNull(result);
        assertEquals(userViewDTO, result);

        // Проверка вызовов методов
        verify(principal, times(1)).getName();
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(modelMapper, times(1)).map(optionalUser, UserViewDTO.class);
    }

    @Test
    void testGetAuthorTasks() {
        Integer pageNumber = 0;
        User user = new User();
        Long userId = 1L;
        user.setId(userId);

        Task task = new Task();
        task.setId(1L);
        Page<Task> taskPage = new PageImpl<>(Collections.singletonList(task));

        when(taskRepository.findByAuthor(PageRequest.of(pageNumber, TaskServiceImpl.PAGE_SIZE), user))
                .thenReturn(taskPage);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(modelMapper.map(any(), eq(UserViewDTO.class))).thenReturn(new UserViewDTO());
        when(modelMapper.map(any(Task.class), eq(TaskViewDTO.class))).thenReturn(new TaskViewDTO());

        Page<TaskViewDTO> resultPage = userService.getAuthorTasks(pageNumber, userId);
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
    }

    @Test
    void testGetExecutorTasks() {
        Integer pageNumber = 0;
        User user = new User();
        Long userId = 1L;
        user.setId(userId);

        Task task = new Task();
        task.setId(1L);

        Page<Task> taskPage = new PageImpl<>(Collections.singletonList(task));

        when(taskRepository.findByExecutor(PageRequest.of(pageNumber, TaskServiceImpl.PAGE_SIZE), user))
                .thenReturn(taskPage);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(modelMapper.map(any(), eq(UserViewDTO.class))).thenReturn(new UserViewDTO());
        when(modelMapper.map(any(Task.class), eq(TaskViewDTO.class))).thenReturn(new TaskViewDTO());

        Page<TaskViewDTO> resultPage = userService.getExecutorTasks(pageNumber, userId);

        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
    }
}


