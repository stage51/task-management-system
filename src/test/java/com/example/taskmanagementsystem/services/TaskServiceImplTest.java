package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.constants.enums.Status;
import com.example.taskmanagementsystem.dtos.CommentDTO;
import com.example.taskmanagementsystem.dtos.TaskDTO;
import com.example.taskmanagementsystem.dtos.views.CommentViewDTO;
import com.example.taskmanagementsystem.dtos.views.TaskViewDTO;
import com.example.taskmanagementsystem.models.Comment;
import com.example.taskmanagementsystem.models.Task;
import com.example.taskmanagementsystem.repositories.CommentRepository;
import com.example.taskmanagementsystem.repositories.TaskRepository;
import com.example.taskmanagementsystem.repositories.UserRepository;
import com.example.taskmanagementsystem.services.impl.TaskServiceImpl;
import com.example.taskmanagementsystem.utils.ValidationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.taskmanagementsystem.services.AbstractService.PAGE_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentService commentService;

    @Test
    void testCreateTask() {
        TaskDTO taskDTO = new TaskDTO();
        Task task = new Task();
        TaskViewDTO taskViewDTO = new TaskViewDTO();

        when(validationUtils.isValid(taskDTO)).thenReturn(true);
        when(taskRepository.saveAndFlush(task)).thenReturn(task);
        when(modelMapper.map(taskDTO, Task.class)).thenReturn(task);
        when(modelMapper.map(task, TaskViewDTO.class)).thenReturn(taskViewDTO);

        TaskViewDTO result = taskService.create(taskDTO);

        assertEquals(taskViewDTO, result);

        verify(validationUtils, times(1)).isValid(taskDTO);
        verify(taskRepository, times(1)).saveAndFlush(task);
        verify(modelMapper, times(1)).map(taskDTO, Task.class);
        verify(modelMapper, times(1)).map(task, TaskViewDTO.class);
    }

    @Test
    void testGetTask() {
        Long taskId = 1L;
        Task task = new Task();
        TaskViewDTO taskViewDTO = new TaskViewDTO();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(modelMapper.map(task, TaskViewDTO.class)).thenReturn(taskViewDTO);

        TaskViewDTO result = taskService.get(taskId);

        assertEquals(taskViewDTO, result);

        verify(taskRepository, times(1)).findById(taskId);
    }
    @Test
    void testUpdateTask() {
        Long taskId = 1L;
        TaskDTO taskDTO = new TaskDTO();
        Task task = new Task();
        Optional<Task> optionalTask = Optional.of(task);
        TaskViewDTO taskViewDTO = new TaskViewDTO();

        when(taskRepository.findById(taskId)).thenReturn(optionalTask);
        when(validationUtils.isValid(taskDTO)).thenReturn(true);
        when(taskRepository.saveAndFlush(task)).thenReturn(task);
        when(modelMapper.map(task, TaskViewDTO.class)).thenReturn(taskViewDTO);

        TaskViewDTO result = taskService.update(taskId, taskDTO);

        assertEquals(taskViewDTO, result);

        verify(taskRepository, times(1)).findById(taskId);
        verify(validationUtils, times(1)).isValid(taskDTO);
        verify(taskRepository, times(1)).saveAndFlush(task);
        verify(modelMapper, times(1)).map(task, TaskViewDTO.class);
    }

    @Test
    void testDeleteTask() {
        Long taskId = 1L;
        Task task = new Task();
        Optional<Task> optionalTask = Optional.of(task);

        when(taskRepository.findById(taskId)).thenReturn(optionalTask);

        taskService.delete(taskId);

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void testGetAllTasks() {
        List<Task> tasks = Arrays.asList(new Task(), new Task());
        List<TaskViewDTO> taskViewDTOs = Arrays.asList(new TaskViewDTO(), new TaskViewDTO());

        when(taskRepository.findAll()).thenReturn(tasks);
        when(modelMapper.map(any(), eq(TaskViewDTO.class))).thenReturn(taskViewDTOs.get(0), taskViewDTOs.get(1));

        List<TaskViewDTO> result = taskService.getAll();

        assertEquals(taskViewDTOs, result);

        verify(taskRepository, times(1)).findAll();
        verify(modelMapper, times(2)).map(any(), eq(TaskViewDTO.class));
    }
    @Test
    void testGetPage() {
        List<Task> taskList = IntStream.range(0, PAGE_SIZE)
                .mapToObj(i -> {
                    Task task = new Task();
                    task.setId((long) i);
                    return task;
                })
                .collect(Collectors.toList());

        Page<Task> taskPage = new PageImpl<>(taskList);

        when(taskRepository.findAll(PageRequest.of(0, PAGE_SIZE, Sort.by("priority").ascending()))).thenReturn(taskPage);

        when(modelMapper.map(any(), eq(TaskViewDTO.class))).thenAnswer(invocation -> {
            Task source = invocation.getArgument(0);
            TaskViewDTO destination = new TaskViewDTO();
            destination.setId(source.getId());
            return destination;
        });

        Page<TaskViewDTO> resultPage = taskService.getPage(0);

        assertNotNull(resultPage);
        assertEquals(taskPage.getTotalElements(), resultPage.getTotalElements());
        assertEquals(taskPage.getNumber(), resultPage.getNumber());
        assertEquals(taskPage.getSize(), resultPage.getSize());
        assertEquals(taskList.size(), resultPage.getContent().size());

        verify(taskRepository, times(1)).findAll(PageRequest.of(0, PAGE_SIZE, Sort.by("priority").ascending()));
        verify(modelMapper, times(PAGE_SIZE)).map(any(), eq(TaskViewDTO.class));
    }

    @Test
    void testGetComments() {
        Long taskId = 1L;
        Task task = new Task();
        CommentViewDTO commentViewDTO = new CommentViewDTO();
        List<CommentViewDTO> commentViewDTOs = Arrays.asList(commentViewDTO, commentViewDTO);
        task.setComments(List.of(new Comment(), new Comment()));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(modelMapper.map(any(), eq(TaskViewDTO.class))).thenAnswer(invocation -> {
            TaskViewDTO destination = new TaskViewDTO();
            destination.setId(taskId);
            return destination;
        });
        when(modelMapper.map(any(), eq(CommentViewDTO.class))).thenReturn(commentViewDTO);
        List<CommentViewDTO> result = taskService.getComments(taskId);

        assertEquals(commentViewDTOs, result);
        verify(taskRepository, times(1)).findById(taskId);
        verify(modelMapper, times(2)).map(any(), eq(CommentViewDTO.class));
    }

    @Test
    void testGetPageComments() {
        Long taskId = 1L;
        Integer pageNumber = 0;

        Task task = new Task();
        task.setId(taskId);
        Optional<Task> optionalTask = Optional.of(task);
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < PAGE_SIZE; i++) {
            Comment comment = new Comment();
            comment.setId((long) i);
            comments.add(comment);
        }

        Page<Comment> commentPage = new PageImpl<>(comments);

        Page<CommentViewDTO> expectedPage = new PageImpl<>(
                comments.stream()
                        .map(comment -> {
                            CommentViewDTO commentViewDTO = new CommentViewDTO();
                            commentViewDTO.setId(comment.getId());
                            return commentViewDTO;
                        })
                        .collect(Collectors.toList())
        );
        when(taskRepository.findById(any())).thenReturn(optionalTask);
        when(commentRepository.findByTask(PageRequest.of(pageNumber, PAGE_SIZE), task)).thenReturn(commentPage);
        when(modelMapper.map(any(), eq(TaskViewDTO.class))).thenAnswer(invocation -> {
            TaskViewDTO destination = new TaskViewDTO();
            return destination;
        });

        var ref = new Object() {
            Long i = 0L;
        };
        when(modelMapper.map(any(), eq(CommentViewDTO.class))).thenAnswer(invocation -> {
            CommentViewDTO commentViewDTO = new CommentViewDTO();
            commentViewDTO.setId(ref.i++);
            return commentViewDTO;
        });

        Page<CommentViewDTO> resultPage = taskService.getPageComments(taskId, pageNumber);

        assertEquals(expectedPage.getTotalElements(), resultPage.getTotalElements());
        assertEquals(expectedPage.getNumber(), resultPage.getNumber());
        assertEquals(expectedPage.getSize(), resultPage.getSize());

        verify(commentRepository, times(1)).findByTask(PageRequest.of(pageNumber, PAGE_SIZE), task);
        verify(modelMapper, times(PAGE_SIZE)).map(any(), eq(CommentViewDTO.class));
    }

    @Test
    void testWriteComment() {
        Long taskId = 1L;
        Long authorId = 2L;
        CommentDTO commentDTO = new CommentDTO();
        CommentViewDTO commentViewDTO = new CommentViewDTO();

        when(commentService.create(commentDTO)).thenReturn(commentViewDTO);

        CommentViewDTO result = taskService.writeComment(taskId, authorId, commentDTO);

        assertEquals(commentViewDTO, result);

        verify(commentService, times(1)).create(commentDTO);
    }

    @Test
    void testChangeStatus() {
        Long taskId = 1L;
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setStatus(Status.PROCESS);

        Task task = new Task();
        task.setStatus(Status.WAITING);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(validationUtils.isValid(taskDTO)).thenReturn(true);
        when(modelMapper.map(task, TaskViewDTO.class)).thenAnswer(invocation -> {
            TaskViewDTO taskViewDTO = new TaskViewDTO();
            taskViewDTO.setStatus(taskDTO.getStatus());
            return taskViewDTO;
        });
        when(taskRepository.saveAndFlush(any())).thenReturn(task);

        TaskViewDTO result = taskService.changeStatus(taskId, taskDTO);

        assertEquals(taskDTO.getStatus(), result.getStatus());

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).saveAndFlush(task);
        verify(modelMapper, times(1)).map(task, TaskViewDTO.class);
    }

}
