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

    // Trouver par User Story
    List<Task> findByUserStoryId(Long userStoryId);

    // Trouver par Sprint Backlog
    List<Task> findBySprintBacklogId(Long sprintBacklogId);

    // Trouver par utilisateur assigné
    List<Task> findByAssignedToId(Long userId);

    // Trouver par statut
    List<Task> findByStatus(TaskStatus status);

    // Trouver par utilisateur et statut
    List<Task> findByAssignedToIdAndStatus(Long userId, TaskStatus status);

    // Tâches non assignées dans un sprint
    List<Task> findBySprintBacklogIdAndAssignedToIsNull(Long sprintBacklogId);

    // Calculer les heures totales estimées pour un sprint
    @Query("SELECT SUM(t.estimatedHours) FROM Task t WHERE t.sprintBacklog.id = :sprintBacklogId")
    Double calculateTotalEstimatedHours(@Param("sprintBacklogId") Long sprintBacklogId);

    // Calculer les heures totales réelles pour un sprint
    @Query("SELECT SUM(t.actualHours) FROM Task t WHERE t.sprintBacklog.id = :sprintBacklogId")
    Double calculateTotalActualHours(@Param("sprintBacklogId") Long sprintBacklogId);
}



