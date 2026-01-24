package org.example.scrum.repository;

import org.example.scrum.entities.Task;
import org.example.scrum.entities.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserStoryId(Long userStoryId);

    List<Task> findBySprintBacklogId(Long sprintBacklogId);

    List<Task> findByAssignedToId(Long userId);

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByAssignedToIdAndStatus(Long userId, TaskStatus status);

    List<Task> findBySprintBacklogIdAndAssignedToIsNull(Long sprintBacklogId);

    List<Task> findByUserStoryIdOrderByTaskOrderAsc(Long userStoryId);

}



