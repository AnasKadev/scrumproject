package org.example.scrum.service;

import org.example.scrum.dto.CommentDTO;
import org.example.scrum.dto.CreateCommentRequest;
import org.example.scrum.dto.UpdateCommentRequest;
import org.example.scrum.entities.Comment;
import org.example.scrum.entities.Task;
import org.example.scrum.entities.User;
import org.example.scrum.entities.UserStory;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.mapper.CommentMapper;
import org.example.scrum.repository.CommentRepository;
import org.example.scrum.repository.TaskRepository;
import org.example.scrum.repository.UserRepository;
import org.example.scrum.repository.UserStoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    private Comment testComment;
    private CommentDTO testCommentDTO;
    private User testUser;
    private UserStory testUserStory;
    private Task testTask;
    private CreateCommentRequest createRequest;
    private UpdateCommentRequest updateRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstname("John");
        testUser.setLastname("Doe");

        testUserStory = new UserStory();
        testUserStory.setId(1L);
        testUserStory.setTitle("Test Story");

        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");

        testComment = new Comment();
        testComment.setId(1L);
        testComment.setContent("Test Comment");
        testComment.setAuthor(testUser);
        testComment.setUserStory(testUserStory);
        testComment.setEdited(false);

        testCommentDTO = new CommentDTO();
        testCommentDTO.setId(1L);
        testCommentDTO.setContent("Test Comment");
        testCommentDTO.setAuthorId(1L);

        createRequest = new CreateCommentRequest();
        createRequest.setContent("Test Comment");
        createRequest.setUserStoryId(1L);

        updateRequest = new UpdateCommentRequest();
        updateRequest.setContent("Updated Comment");
    }

    @Test
    void createComment_OnUserStory_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(testUserStory));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        when(commentMapper.toDTO(any(Comment.class))).thenReturn(testCommentDTO);

        // Act
        CommentDTO result = commentService.createComment(createRequest, 1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Comment", result.getContent());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void createComment_OnTask_Success() {
        // Arrange
        createRequest.setUserStoryId(null);
        createRequest.setTaskId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        when(commentMapper.toDTO(any(Comment.class))).thenReturn(testCommentDTO);

        // Act
        CommentDTO result = commentService.createComment(createRequest, 1L);

        // Assert
        assertNotNull(result);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void createComment_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.createComment(createRequest, 999L);
        });
    }

    @Test
    void createComment_UserStoryNotFound_ThrowsException() {
        // Arrange
        createRequest.setUserStoryId(999L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userStoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.createComment(createRequest, 1L);
        });
    }

    @Test
    void updateComment_Success() {
        // Arrange
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        when(commentMapper.toDTO(any(Comment.class))).thenReturn(testCommentDTO);

        // Act
        CommentDTO result = commentService.updateComment(1L, updateRequest, 1L);

        // Assert
        assertNotNull(result);
        verify(commentRepository, times(1)).save(argThat(comment -> comment.isEdited()));
    }

    @Test
    void updateComment_NotAuthor_ThrowsException() {
        // Arrange
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            commentService.updateComment(1L, updateRequest, 999L);
        });
    }

    @Test
    void updateComment_NotFound_ThrowsException() {
        // Arrange
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.updateComment(999L, updateRequest, 1L);
        });
    }

    @Test
    void getCommentById_Success() {
        // Arrange
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        when(commentMapper.toDTO(any(Comment.class))).thenReturn(testCommentDTO);

        // Act
        CommentDTO result = commentService.getCommentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getAllComments_Success() {
        // Arrange
        List<Comment> comments = Arrays.asList(testComment);
        List<CommentDTO> commentDTOs = Arrays.asList(testCommentDTO);
        when(commentRepository.findAll()).thenReturn(comments);
        when(commentMapper.toDTOList(comments)).thenReturn(commentDTOs);

        // Act
        List<CommentDTO> results = commentService.getAllComments();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void getCommentsByUserStoryId_Success() {
        // Arrange
        List<Comment> comments = Arrays.asList(testComment);
        List<CommentDTO> commentDTOs = Arrays.asList(testCommentDTO);
        when(commentRepository.findByUserStoryId(1L)).thenReturn(comments);
        when(commentMapper.toDTOList(comments)).thenReturn(commentDTOs);

        // Act
        List<CommentDTO> results = commentService.getCommentsByUserStoryId(1L);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void getCommentsByTaskId_Success() {
        // Arrange
        List<Comment> comments = Arrays.asList(testComment);
        List<CommentDTO> commentDTOs = Arrays.asList(testCommentDTO);
        when(commentRepository.findByTaskId(1L)).thenReturn(comments);
        when(commentMapper.toDTOList(comments)).thenReturn(commentDTOs);

        // Act
        List<CommentDTO> results = commentService.getCommentsByTaskId(1L);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void getCommentsByAuthorId_Success() {
        // Arrange
        List<Comment> comments = Arrays.asList(testComment);
        List<CommentDTO> commentDTOs = Arrays.asList(testCommentDTO);
        when(commentRepository.findByAuthorId(1L)).thenReturn(comments);
        when(commentMapper.toDTOList(comments)).thenReturn(commentDTOs);

        // Act
        List<CommentDTO> results = commentService.getCommentsByAuthorId(1L);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void deleteComment_Success() {
        // Arrange
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        doNothing().when(commentRepository).delete(any(Comment.class));

        // Act
        commentService.deleteComment(1L, 1L);

        // Assert
        verify(commentRepository, times(1)).delete(any(Comment.class));
    }

    @Test
    void deleteComment_NotAuthor_ThrowsException() {
        // Arrange
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            commentService.deleteComment(1L, 999L);
        });
    }

    @Test
    void deleteComment_NotFound_ThrowsException() {
        // Arrange
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.deleteComment(999L, 1L);
        });
    }
}

