package org.example.scrum.repository;

import org.example.scrum.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUserStoryId(Long userStoryId);

    List<Comment> findByTaskId(Long taskId);

    List<Comment> findByAuthorId(Long authorId);

    List<Comment> findByUserStoryIdOrderByCreatedAtDesc(Long userStoryId);

    List<Comment> findByTaskIdOrderByCreatedAtDesc(Long taskId);
}

