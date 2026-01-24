package org.example.scrum.service;

import lombok.RequiredArgsConstructor;
import org.example.scrum.dto.CommentDTO;
import org.example.scrum.dto.CreateCommentRequest;
import org.example.scrum.dto.UpdateCommentRequest;
import org.example.scrum.entities.Comment;
import org.example.scrum.entities.Task;
import org.example.scrum.entities.User;
import org.example.scrum.entities.UserStory;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.repository.CommentRepository;
import org.example.scrum.repository.TaskRepository;
import org.example.scrum.repository.UserRepository;
import org.example.scrum.repository.UserStoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final UserStoryRepository userStoryRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public CommentDTO createComment(CreateCommentRequest request, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + authorId));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAuthor(author);

        if (request.getUserStoryId() != null) {
            UserStory userStory = userStoryRepository.findById(request.getUserStoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("User Story non trouvée avec l'ID: " + request.getUserStoryId()));
            comment.setUserStory(userStory);
        }

        if (request.getTaskId() != null) {
            Task task = taskRepository.findById(request.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tâche non trouvée avec l'ID: " + request.getTaskId()));
            comment.setTask(task);
        }

        Comment saved = commentRepository.save(comment);
        return convertToDTO(saved);
    }

    @Transactional
    public CommentDTO updateComment(Long id, UpdateCommentRequest request, Long authorId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commentaire non trouvé avec l'ID: " + id));

        // Vérifier que l'utilisateur est l'auteur du commentaire
        if (!comment.getAuthor().getId().equals(authorId)) {
            throw new IllegalArgumentException("Vous n'êtes pas autorisé à modifier ce commentaire");
        }

        if (request.getContent() != null) {
            comment.setContent(request.getContent());
            comment.setEdited(true);
        }

        Comment updated = commentRepository.save(comment);
        return convertToDTO(updated);
    }

    @Transactional(readOnly = true)
    public CommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commentaire non trouvé avec l'ID: " + id));
        return convertToDTO(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getAllComments() {
        return commentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByUserStoryId(Long userStoryId) {
        return commentRepository.findByUserStoryId(userStoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByAuthorId(Long authorId) {
        return commentRepository.findByAuthorId(authorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Long id, Long authorId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commentaire non trouvé avec l'ID: " + id));

        // Vérifier que l'utilisateur est l'auteur du commentaire
        if (!comment.getAuthor().getId().equals(authorId)) {
            throw new IllegalArgumentException("Vous n'êtes pas autorisé à supprimer ce commentaire");
        }

        commentRepository.delete(comment);
    }

    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setAuthorId(comment.getAuthor().getId());
        dto.setAuthorName(comment.getAuthor().getFirstname() + " " + comment.getAuthor().getLastname());

        if (comment.getUserStory() != null) {
            dto.setUserStoryId(comment.getUserStory().getId());
            dto.setUserStoryTitle(comment.getUserStory().getTitle());
        }

        if (comment.getTask() != null) {
            dto.setTaskId(comment.getTask().getId());
            dto.setTaskTitle(comment.getTask().getTitle());
        }

        dto.setEdited(comment.isEdited());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());

        return dto;
    }
}

