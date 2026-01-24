package org.example.scrum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.scrum.dto.CommentDTO;
import org.example.scrum.dto.CreateCommentRequest;
import org.example.scrum.dto.UpdateCommentRequest;
import org.example.scrum.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(
            @Valid @RequestBody CreateCommentRequest request,
            @RequestParam Long authorId) {
        CommentDTO created = commentService.createComment(request, authorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCommentRequest request,
            @RequestParam Long authorId) {
        CommentDTO updated = commentService.updateComment(id, request, authorId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        CommentDTO comment = commentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        List<CommentDTO> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/user-story/{userStoryId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByUserStoryId(@PathVariable Long userStoryId) {
        List<CommentDTO> comments = commentService.getCommentsByUserStoryId(userStoryId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByTaskId(@PathVariable Long taskId) {
        List<CommentDTO> comments = commentService.getCommentsByTaskId(taskId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByAuthorId(@PathVariable Long authorId) {
        List<CommentDTO> comments = commentService.getCommentsByAuthorId(authorId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            @RequestParam Long authorId) {
        commentService.deleteComment(id, authorId);
        return ResponseEntity.noContent().build();
    }
}

