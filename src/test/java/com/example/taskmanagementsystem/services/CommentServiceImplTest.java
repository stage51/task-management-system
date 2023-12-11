package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.dtos.CommentDTO;
import com.example.taskmanagementsystem.dtos.views.CommentViewDTO;
import com.example.taskmanagementsystem.models.Comment;
import com.example.taskmanagementsystem.models.Task;
import com.example.taskmanagementsystem.models.User;
import com.example.taskmanagementsystem.repositories.CommentRepository;
import com.example.taskmanagementsystem.repositories.TaskRepository;
import com.example.taskmanagementsystem.repositories.UserRepository;
import com.example.taskmanagementsystem.services.impl.CommentServiceImpl;
import com.example.taskmanagementsystem.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {
    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateComment() {
        CommentDTO commentDTO = new CommentDTO();
        Comment comment = new Comment();
        CommentViewDTO commentViewDTO = new CommentViewDTO();

        when(validationUtils.isValid(commentDTO)).thenReturn(true);
        when(modelMapper.map(commentDTO, Comment.class)).thenReturn(comment);
        when(userRepository.findById(commentDTO.getAuthor())).thenReturn(Optional.of(new User()));
        when(taskRepository.findById(commentDTO.getTask())).thenReturn(Optional.of(new Task()));
        when(commentRepository.saveAndFlush(comment)).thenReturn(comment);
        when(modelMapper.map(comment, CommentViewDTO.class)).thenReturn(commentViewDTO);

        CommentViewDTO result = commentService.create(commentDTO);

        assertEquals(commentViewDTO, result);

        verify(validationUtils, times(1)).isValid(commentDTO);
        verify(userRepository, times(1)).findById(commentDTO.getAuthor());
        verify(taskRepository, times(1)).findById(commentDTO.getTask());
        verify(commentRepository, times(1)).saveAndFlush(comment);
        verify(modelMapper, times(1)).map(comment, CommentViewDTO.class);
    }
    @Test
    void testGetComment() {
        Long commentId = 1L;
        Comment comment = new Comment();
        CommentViewDTO commentViewDTO = new CommentViewDTO();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(modelMapper.map(comment, CommentViewDTO.class)).thenReturn(commentViewDTO);

        CommentViewDTO result = commentService.get(commentId);

        assertEquals(commentViewDTO, result);

        verify(commentRepository, times(1)).findById(commentId);
    }
    @Test
    void testUpdateComment() {
        Long commentId = 1L;
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setAuthor(1L);
        commentDTO.setContent("Updated content");
        commentDTO.setTask(2L);

        Comment existingComment = new Comment();
        existingComment.setId(commentId);

        Comment updatedComment = new Comment();
        updatedComment.setId(commentId);
        updatedComment.setAuthor(new User());
        updatedComment.setContent("Updated content");
        updatedComment.setTask(new Task());

        CommentViewDTO commentViewDTO = new CommentViewDTO();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));

        when(validationUtils.isValid(commentDTO)).thenReturn(true);
        when(userRepository.findById(commentDTO.getAuthor())).thenReturn(Optional.of(new User()));
        when(taskRepository.findById(commentDTO.getTask())).thenReturn(Optional.of(new Task()));
        when(commentRepository.saveAndFlush(any(Comment.class))).thenReturn(updatedComment);
        when(modelMapper.map(updatedComment, CommentViewDTO.class)).thenReturn(commentViewDTO);

        CommentViewDTO result = commentService.update(commentId, commentDTO);

        assertEquals(commentViewDTO, result);

        verify(validationUtils, times(1)).isValid(commentDTO);
        verify(commentRepository, times(1)).findById(commentId);
        verify(userRepository, times(1)).findById(commentDTO.getAuthor());
        verify(taskRepository, times(1)).findById(commentDTO.getTask());
        verify(commentRepository, times(1)).saveAndFlush(any(Comment.class));
        verify(modelMapper, times(1)).map(updatedComment, CommentViewDTO.class);
    }


    @Test
    void testDeleteComment() {
        Long commentId = 1L;
        Comment comment = new Comment();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        commentService.delete(commentId);

        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void testGetAllComments() {
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);

        when(commentRepository.findAll()).thenReturn(comments);
        when(modelMapper.map(comment1, CommentViewDTO.class)).thenReturn(new CommentViewDTO());
        when(modelMapper.map(comment2, CommentViewDTO.class)).thenReturn(new CommentViewDTO());

        assertEquals(2, commentService.getAll().size());

        verify(commentRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(comment1, CommentViewDTO.class);
        verify(modelMapper, times(1)).map(comment2, CommentViewDTO.class);
    }

    @Test
    void testGetPageComments() {
        int pageNumber = 0;
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);

        when(commentRepository.findAll(PageRequest.of(pageNumber, CommentServiceImpl.PAGE_SIZE)))
                .thenReturn(new PageImpl<>(comments));
        when(modelMapper.map(comment1, CommentViewDTO.class)).thenReturn(new CommentViewDTO());
        when(modelMapper.map(comment2, CommentViewDTO.class)).thenReturn(new CommentViewDTO());

        assertEquals(2, commentService.getPage(pageNumber).toList().size());

        verify(commentRepository, times(1)).findAll(PageRequest.of(pageNumber, CommentServiceImpl.PAGE_SIZE));
        verify(modelMapper, times(1)).map(comment1, CommentViewDTO.class);
        verify(modelMapper, times(1)).map(comment2, CommentViewDTO.class);
    }
}
